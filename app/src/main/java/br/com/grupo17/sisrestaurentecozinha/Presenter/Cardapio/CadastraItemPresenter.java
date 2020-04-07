package br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.PermissaoUtil;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class CadastraItemPresenter implements Cardapio.CadastroPresenter {
    private Boolean reUpItem, reUpItemUpdate, reUpFoto, reUpPathFoto, reDeleteFoto, reDeletaItem, isUpdate;
    private String pathImgDeletar, categoriaAntiga, idAntigo;
    private Cardapio.ModelCardapio model;
    private PermissaoUtil permissaoUtil;
    private Cardapio.CadastroView view;
    private CardapioObj cardapioObj;
    private Uri pathImg;

    public CadastraItemPresenter(Cardapio.CadastroView view) {
        this.view = view;
        this.reUpItem = false;
        this.reUpFoto = false;
        this.isUpdate = false;
        this.reUpPathFoto = false;
        this.reDeleteFoto = false;
        this.reDeletaItem = false;
        this.reUpItemUpdate = false;
        this.model = new ModelDb(this);
        this.permissaoUtil = new PermissaoUtil(this);
    }

    public CadastraItemPresenter(Cardapio.CadastroView view, CardapioObj cardapioObj) {
        this.view = view;
        this.isUpdate = true;
        this.reUpItem = false;
        this.reUpFoto = false;
        this.reUpPathFoto = false;
        this.reDeleteFoto = false;
        this.reDeletaItem = false;
        this.reUpItemUpdate = false;
        this.cardapioObj = cardapioObj;
        this.model = new ModelDb(this);
        this.permissaoUtil = new PermissaoUtil(this);
    }

    @Override
    public void abreGaleria() {
        view.abrirGaleria();
    }

    public boolean fecharTela() {
        HashMap<String, String> map = view.getInformacao();
        if (cardapioObj == null) {
            return !TextUtils.isEmpty(map.get(getContext().getString(R.string.key_campo_nome_prato))) || !TextUtils.isEmpty(map.get(getContext().getString(R.string.campo_descricao))) || !TextUtils.isEmpty(map.get(getContext().getString(R.string.key_campo_preco))) || pathImg != null;
        } else {
            CardapioObj obj = new CardapioObj(getContext(), map);
            obj.setPathFoto(pathImg != null ? pathImg.getPath() : cardapioObj.getPathFoto());
            obj.setId(cardapioObj.getId());
            obj.setAvaliacao(cardapioObj.getAvaliacao());
            obj.setQuantidadeAV(cardapioObj.getQuantidadeAV());
            return !cardapioObj.equals(obj);
        }
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public CardapioObj getCardapio() {
        return cardapioObj;
    }

    private void getExceptionTask(Boolean progress, Task task, String mensagem) {
        try {
            if (!progress) {
                view.setProgressVisibility(progress);
            }
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContext(), mensagem + ",\n" + e.getMessage());
        }
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 12 && data.getData() != null) {
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(data.getData(), filePath, null, null, null);
                if (c != null) {
                    c.moveToFirst();
                    pathImg = Uri.parse("file://" + c.getString(c.getColumnIndex(filePath[0])));
                    c.close();
                    view.setImagemImageView(pathImg);
                }
            }
        }
    }

    @Override
    public void responseDeletaFoto(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reDeleteFoto) {
                reDeleteFoto = true;
                model.criaRefDeletaFotoComUri(pathImg);
            } else {
                reDeleteFoto = false;
                getExceptionTask(false, task, getContext().getString(R.string.erro_deletar_foto));
                if (isUpdate) {
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_atualizado_sem_foto));
                    view.fechaComResult();
                } else {
                    view.limpaCampos();
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_salvo_sem_foto));
                    view.setButtonEnabled(true);
                }
            }
        } else {
            view.setProgressVisibility(false);
            if (isUpdate) {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_update_feito));
                view.fechaComResult();
            } else {
                view.limpaCampos();
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_salvo_sem_foto));
                view.setButtonEnabled(true);
            }
        }
    }

    @Override
    public void responseDeletaItem(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reDeletaItem) {
                reDeletaItem = true;
                model.deletaItem(categoriaAntiga, idAntigo);
            } else {
                reDeletaItem = false;
                getExceptionTask(false, task, getContext().getString(R.string.erro_deletar_categoria_antiga));
                if (pathImg != null) {
                    model.salvaFotoItem(pathImg, cardapioObj.getCategoria());
                } else {
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_update_feito));
                    view.fechaComResult();
                }
            }
        } else {
            if (pathImg != null) {
                model.salvaFotoItem(pathImg, cardapioObj.getCategoria());
            } else {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_update));
                view.fechaComResult();
            }
        }
    }

    @Override
    public void responseSaltaFoto(Task<Uri> task) {
        if (!task.isSuccessful()) {
            if (!reUpFoto) {
                reUpFoto = true;
                model.salvaFotoItem(pathImg, cardapioObj.getCategoria());
            } else {
                reUpFoto = false;
                getExceptionTask(false, task, getContext().getString(R.string.erro_upload_foto));
                if (isUpdate) {
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_foto_antiga));
                    view.fechaComResult();
                } else {
                    view.limpaCampos();
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_salvo_sem_foto));
                    view.setButtonEnabled(true);
                }
            }
        } else {
            if (task.getResult() != null) {
                reUpFoto = false;
                if (!TextUtils.isEmpty(cardapioObj.getPathFoto())) {
                    pathImgDeletar = cardapioObj.getPathFoto();
                }
                cardapioObj.setPathFoto(task.getResult().toString());
                model.updateCampoPathFoto(cardapioObj.getCategoria(), cardapioObj.getId(), cardapioObj.getPathFoto());
            }
        }
    }

    @Override
    public void responseSalvaItem(Task<DocumentReference> task) {
        if (!task.isSuccessful()) {
            if (!reUpItem) {
                reUpItem = true;
                model.salvaCardapioObj(cardapioObj);
            } else {
                reUpItem = false;
                view.setButtonEnabled(true);
                getExceptionTask(false, task, getContext().getString(R.string.erro_ao_salvar_item));
            }
        } else {
            if (task.getResult() != null) {
                reUpItem = false;
                idAntigo = cardapioObj.getId();
                cardapioObj.setId(task.getResult().getId());
                if (isUpdate) {
                    model.deletaItem(categoriaAntiga, idAntigo);
                } else {
                    model.salvaFotoItem(pathImg, cardapioObj.getCategoria());
                }
            }
        }
    }

    @Override
    public void responseUpdateCampoPathFoto(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reUpPathFoto) {
                reUpPathFoto = true;
                model.updateCampoPathFoto(cardapioObj.getCategoria(), cardapioObj.getId(), cardapioObj.getPathFoto());
            } else {
                reUpPathFoto = false;
                getExceptionTask(true, task, getContext().getString(R.string.erro_link_foto));

                if (isUpdate) {
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_finalizando_update) + "\n" + getContext().getString(R.string.aviso_foto_antiga));
                } else {
                    cardapioObj = null;
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_finalizando_sem_foto));
                    view.setButtonEnabled(true);
                }
                model.criaRefDeletaFotoComUri(pathImg);
            }
        } else {
            if (isUpdate) {
                model.criaRefDeletaFotoComUrl(pathImgDeletar);
            } else {
                view.setProgressVisibility(false);
                pathImg = null;
                cardapioObj = null;
                view.limpaCampos();
                reUpPathFoto = false;
                view.setButtonEnabled(true);
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_cadastro));
            }
        }
    }

    @Override
    public void responseUpdateItem(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reUpItemUpdate) {
                reUpItemUpdate = true;
                model.updateItem(cardapioObj);
            } else {
                reUpItemUpdate = false;
                getExceptionTask(false, task, getContext().getString(R.string.erro_ao_atualizar_item));
                view.setButtonEnabled(true);
            }
        } else {
            if (pathImg != null) {
                model.salvaFotoItem(pathImg, cardapioObj.getCategoria());
            } else {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_update));
                view.fechaComResult();
            }
        }
    }

    @Override
    public void salvaCardapioObj() {
        HashMap<String, String> map = view.getInformacao();
        if (validaCampos(map)) {
            view.setProgressVisibility(true);
            view.setButtonEnabled(false);
            if (cardapioObj == null) {
                map.put(getContext().getString(R.string.key_campo_foto), "");
                cardapioObj = new CardapioObj(getContext(), map);
                model.salvaCardapioObj(cardapioObj);
            } else {
                CardapioObj obj = new CardapioObj(getContext(), map);
                obj.setId(cardapioObj.getId());
                obj.setAvaliacao(cardapioObj.getAvaliacao());
                obj.setQuantidadeAV(cardapioObj.getQuantidadeAV());
                obj.setPathFoto(cardapioObj.getPathFoto());
                cardapioObj = obj;

                if (!TextUtils.isEmpty(categoriaAntiga)) {
                    model.salvaCardapioObj(cardapioObj);
                } else {
                    model.updateItem(cardapioObj);
                }
            }
        }
    }

    private boolean validaCampos(HashMap<String, String> map) {
        if (TextUtils.isEmpty(map.get(getContext().getString(R.string.key_campo_nome_prato)))) {
            view.setError(R.string.campo_nome, getContext().getResources().getString(R.string.erro_nome_branco));
        } else if (TextUtils.isEmpty(map.get(getContext().getString(R.string.key_campo_descricao)))) {
            view.setError(R.string.campo_descricao, getContext().getResources().getString(R.string.erro_descricao_branco));
        } else if (TextUtils.isEmpty(map.get(getContext().getString(R.string.key_campo_preco)))) {
            view.setError(R.string.campo_preco, getContext().getResources().getString(R.string.erro_preco_branco));
        } else if (cardapioObj == null && (pathImg == null || TextUtils.isEmpty(pathImg.toString()))) {
            view.setError(R.string.aviso_imageView, getContext().getResources().getString(R.string.erro_sem_imagem));
        } else if (Objects.equals(map.get(getContext().getString(R.string.key_campo_categoria)), getContext().getString(R.string.campo_spinner))) {
            view.setError(R.string.campo_spinner, getContext().getString(R.string.campo_spinner));
        } else if (!Connectivity.isConnected(getContext())) {
            view.setError(R.string.erro_sem_conexao, getContext().getString(R.string.erro_sem_conexao));
        } else {
            if (cardapioObj != null && !cardapioObj.getCategoria().equals(map.get(getContext().getString(R.string.key_campo_categoria)))) {
                categoriaAntiga = cardapioObj.getCategoria();
            }
            return true;
        }
        return false;
    }

    @Override
    public void verificaPermission() {
        permissaoUtil.requestPermission();
    }
}