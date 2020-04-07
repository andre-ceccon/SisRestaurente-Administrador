package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Cupom;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class GerenciamentoCupomPresenter implements Administracao.GerenciamentoCupomPresenter {
    private Administracao.GerenciamentoCupomView view;
    private boolean reSalva, reRetrive, reDeleta;
    private List<Cupom> cupomList;
    private ModelDb model;
    private Cupom cupom;

    public GerenciamentoCupomPresenter(Administracao.GerenciamentoCupomView view) {
        this.view = view;
        this.reSalva = false;
        this.reDeleta = false;
        this.reRetrive = false;
        this.cupomList = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public void deletaCupom(Cupom cupom) {
        this.cupom = cupom;
        view.setLayoutListVisibility(false);
        view.setProgressVisibility(true);
        model.deletaCupom(cupom.getId());
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public List<Cupom> getList() {
        return cupomList;
    }

    @Override
    public void retriveCupons() {
        view.setProgressVisibility(true);
        model.retriveCupons();
    }

    @Override
    public void responseRetriveCupons(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetrive) {
                reRetrive = true;
                retriveCupons();
            } else {
                view.setProgressVisibility(false);
                SpAndToast.showMessage(getContext(),
                        Objects.requireNonNull(
                                task.getException(), getContext().getString(R.string.erro_consultar) //mensagem que aparecera, caso objeto seja null
                        ).getMessage());
            }
        } else {
            if (task.getResult() != null) {
                view.setProgressVisibility(false);
                if (task.getResult().isEmpty()) {
                    view.setVisibilityList(false);
                } else {
                    cupomList.clear();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        cupomList.add(
                                new Cupom(
                                        doc.getId(),
                                        Objects.requireNonNull(doc.getLong("quantidade")).intValue(),
                                        Objects.requireNonNull(doc.getDouble("desconto")).floatValue(),
                                        Objects.requireNonNull(doc.getLong("limite")).intValue()
                                ));
                    }

                    view.notifyAdapter();
                    view.setVisibilityList(true);
                    view.setLayoutListVisibility(true);
                }
            }
        }
    }

    @Override
    public void responseSalvaCupom(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reSalva) {
                reSalva = true;
                salvarCupom();
            } else {
                view.setProgressVisibility(false);
                view.setLayoutCadVisibility(true);
                SpAndToast.showMessage(getContext(),
                        Objects.requireNonNull(
                                task.getException(), getContext().getString(R.string.erro_ao_salvar_item) //mensagem que aparecera, caso objeto seja null
                        ).getMessage());
            }
        } else {
            view.limparCampos();
            view.setProgressVisibility(false);
            view.setLayoutCadVisibility(true);
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_cadastro));
        }
    }

    @Override
    public void responseDeleteCupon(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reDeleta) {
                reDeleta = true;
                deletaCupom(cupom);
            } else {
                view.setProgressVisibility(false);
                view.setLayoutListVisibility(true);
                SpAndToast.showMessage(getContext(),
                        Objects.requireNonNull(
                                task.getException(), getContext().getString(R.string.erro_deletar_filme) //mensagem que aparecera, caso objeto seja null
                        ).getMessage());
            }
        } else {
            view.setLayoutListVisibility(true);
            view.setProgressVisibility(false);
            cupomList.remove(cupom);

            if (cupomList.isEmpty()) {
                view.setVisibilityList(false);
            } else {
                view.notifyAdapter();
            }
        }
    }

    @Override
    public void salvarCupom() {
        Map<String, Object> map = view.getInformacoes();
        if (validaInfos(map)) {
            view.setProgressVisibility(true);
            view.setLayoutCadVisibility(false);
            map.put("desconto", Float.valueOf(Objects.requireNonNull(map.get("desconto"), "0").toString()));
            map.put("quantidade", Integer.valueOf(Objects.requireNonNull(map.get("quantidade"), "0").toString()));
            model.salvaCupom(map);
        }
    }

    private boolean validaInfos(Map<String, Object> map) {
        if (Objects.requireNonNull(map.get("nome")).toString().isEmpty()) {
            view.setErro("nome", "Preencha o Codigo");
        } else if (Objects.requireNonNull(map.get("quantidade")).toString().isEmpty()) {
            view.setErro("quantidade", "Preencha a Quantidade");
        } else if (Objects.requireNonNull(map.get("desconto")).toString().isEmpty()) {
            view.setErro("desconto", "Preencha o Desconto");
        } else if (!Connectivity.isConnected(getContext())) {
            view.setErro("", getContext().getString(R.string.erro_sem_conexao));
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean fechaTela() {
        Map<String, Object> map = view.getInformacoes();
        return !Objects.requireNonNull(map.get("nome")).toString().isEmpty() ||
                !Objects.requireNonNull(map.get("quantidade")).toString().isEmpty() ||
                !Objects.requireNonNull(map.get("desconto")).toString().isEmpty();
    }
}