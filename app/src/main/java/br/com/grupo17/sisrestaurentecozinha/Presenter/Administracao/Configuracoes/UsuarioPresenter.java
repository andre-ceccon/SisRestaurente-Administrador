package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.User;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class UsuarioPresenter implements Administracao.UsuarioPresenter {
    private boolean reConsulta, reUpdate, reReseta;
    private Administracao.UsuarioView view;
    private String cpf, novoNivel;
    private ModelDb model;
    private User user;

    public UsuarioPresenter(Administracao.UsuarioView view) {
        this.view = view;
        this.reUpdate = false;
        this.reReseta = false;
        this.reConsulta = false;
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContex() {
        return (Context) view;
    }

    private void getException(Task task, String mensagem) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContex(), mensagem);
            SpAndToast.showMessage(getContex(), e.getMessage());
        }
    }

    @Override
    public void retriveUsuario(String cpf) {
        if (TextUtils.isEmpty(cpf)) {
            view.setErro(getContex().getString(R.string.erro_cpf_vazio));
        } else if (cpf.length() < 11) {
            view.setErro(getContex().getString(R.string.erro_cpf_invalido));
        } else if (!Connectivity.isConnected(getContex())) {
            SpAndToast.showMessage(getContex(), getContex().getString(R.string.erro_sem_conexao));
        } else {
            this.cpf = cpf;
            view.setButtonEnabled(R.string.key_usuario_consulta, false);
            view.setProgressVisibility(true);
            model.retriveUsuario(cpf);
        }
    }

    @Override
    public void responseUsuario(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reConsulta) {
                reConsulta = true;
                model.retriveUsuario(cpf);
            } else {
                reConsulta = false;
                view.setProgressVisibility(false);
                getException(task, getContex().getString(R.string.erro_consultar));
                view.setButtonEnabled(R.string.key_usuario_consulta, true);
            }
        } else {
            view.limparCampo();
            reConsulta = false;
            view.setProgressVisibility(false);
            view.setButtonEnabled(R.string.key_usuario_consulta, true);

            if (task.getResult() != null) {
                if (task.getResult().isEmpty()) {
                    SpAndToast.showMessage(getContex(), getContex().getString(R.string.erro_sem_registro_cpf));
                } else {
                    view.setButtonEnabled(R.string.key_usuario_consultaSucesso, true);
                    for (DocumentSnapshot dc : task.getResult().getDocuments()) {
                        user = dc.toObject(User.class);
                        if (user != null) {
                            view.setText(user);
                            user.setId(dc.getId());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void responseUpdate(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reUpdate) {
                reUpdate = true;
                model.updateNivelUser(user.getId(), novoNivel);
            } else {
                reUpdate = false;
                view.setProgressVisibility(false);
                getException(task, getContex().getString(R.string.erro_ao_atualizar_item));
                view.setButtonEnabled(R.string.key_campo_nivelUser, true);
            }
        } else {
            reUpdate = false;
            user.setNivelUser(novoNivel);
            view.setText(user);
            view.setProgressVisibility(false);
            view.setButtonEnabled(R.string.key_campo_nivelUser, true);
            view.setLayVisibility(false);
            SpAndToast.showMessage(getContex(), getContex().getString(R.string.aviso_sucesso_update));
        }
    }

    @Override
    public void responseReset(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reReseta) {
                reReseta = true;
                model.resetaMesa(user.getId());
            } else {
                view.setProgressVisibility(false);
                view.setButtonEnabled(R.string.key_usuario_resetar, true);
                getException(task, getContex().getString(R.string.erro_reset));
            }
        } else {
            view.setProgressVisibility(false);
            view.setButtonEnabled(R.string.key_usuario_resetar, true);
            user.setMesa(getContex().getString(R.string.key_item_nao_disponivel_cardapio));
            view.setText(user);
            SpAndToast.showMessage(getContex(), getContex().getString(R.string.aviso_resetado_sucesso));
        }
    }

    @Override
    public void resetaMesa() {
        if (!Connectivity.isConnected(getContex())) {
            SpAndToast.showMessage(getContex(), getContex().getString(R.string.erro_sem_conexao));
        } else {
            view.setProgressVisibility(true);
            view.setButtonEnabled(R.string.key_usuario_resetar, false);
            model.resetaMesa(user.getId());
        }
    }

    @Override
    public void updateNivel(String nivel) {
        this.novoNivel = nivel;
        view.setProgressVisibility(true);
        view.setButtonEnabled(R.string.key_campo_nivelUser, false);
        model.updateNivelUser(user.getId(), nivel);
    }
}