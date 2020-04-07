package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Relatorios;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaRelatorio;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class CaixaRelatorioPresenter implements Administracao.CaixaRelatorioPresenter {
    private boolean reUpdate, reDelete;
    private Administracao.CaixaRelatorioView view;
    private CaixaRelatorio caixaRelatorio;
    private List<CaixaRelatorio> list;
    private ModelDb model;

    public CaixaRelatorioPresenter(Administracao.CaixaRelatorioView view) {
        this.view = view;
        this.reUpdate = false;
        this.reUpdate = false;
        this.list = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public void deletarItem(CaixaRelatorio relatorio) {
        this.caixaRelatorio = relatorio;
        view.setProgressVisibility(true);
        view.setButtonEnabled(false);
        model.deletaItemCaixa(relatorio.getId());
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public List<CaixaRelatorio> getList() {
        return list;
    }

    @Override
    public void retriveDadosCaixa() {
        model.retriveDadosCaixas();
        view.setProgressVisibility(true);
    }

    @Override
    public void responseDados(QuerySnapshot queryDocumentSnapshots) {
        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    if (!dc.getDocument().getId().equals("manter")) {
                        list.add(getRelatorioAuxiliar(dc));
                    }
                    break;
                case MODIFIED:
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId().equals(dc.getDocument().getId())) {
                            list.set(i, getRelatorioAuxiliar(dc));
                            if (view.getIdSetado().equals(caixaRelatorio.getId())) {
                                view.onClick(caixaRelatorio);
                            }
                            break;
                        }
                    }
                    break;
                case REMOVED:
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId().equals(dc.getDocument().getId())) {
                            list.remove(i);
                            break;
                        }
                    }
                    break;
            }
        }

        sortList();
        view.setProgressVisibility(false);
        view.semItem(list.size() == 0);
    }

    @Override
    public void responseStatus(Task<Void> task) {
        if (!task.isSuccessful()) {
            view.setProgressVisibility(false);
            if (!reUpdate) {
                reUpdate = true;
                updateStatus(caixaRelatorio);
            } else {
                reUpdate = false;
                view.setButtonEnabled(true);
                SpAndToast.showMessage(
                        getContext(), getContext().getString(R.string.erro_ao_atualizar_item)
                );
            }
        } else {
            reUpdate = false;
            view.setButtonEnabled(true);
            SpAndToast.showMessage(
                    getContext(),
                    getContext().getString(R.string.aviso_sucesso_update)
            );
        }
    }

    @Override
    public void responseDelete(Task<Void> task) {
        if (!task.isSuccessful()) {
            view.setProgressVisibility(false);
            if (!reDelete) {
                reDelete = true;
                deletarItem(caixaRelatorio);
            } else {
                reDelete = false;
                view.setButtonEnabled(true);
                SpAndToast.showMessage(
                        getContext(), getContext().getString(R.string.erro_deletar_filme)
                );
            }
        } else {
            reDelete = false;
            SpAndToast.showMessage(
                    getContext(), getContext().getString(R.string.aviso_deletar_sucesso)
            );
            view.setVisibillityList(true);
            view.setButtonEnabled(true);
        }
    }

    @Override
    public void updateStatus(CaixaRelatorio relatorio) {
        this.caixaRelatorio = relatorio;
        view.setProgressVisibility(true);
        view.setButtonEnabled(false);
        model.updateStatusCaixa(relatorio.getId(), "aberto");
    }

    private void sortList() {
        Collections.sort(list, CaixaRelatorio.NameComparator);
        view.notifyAdapter();
    }

    private CaixaRelatorio getRelatorioAuxiliar(DocumentChange dc) {
        return caixaRelatorio = new CaixaRelatorio(
                dc.getDocument().getId(),
                Objects.requireNonNull(dc.getDocument().get("cpf")).toString(),
                dc.getDocument().getDate("data"),
                Objects.requireNonNull(dc.getDocument().get("nomesobrenome")).toString(),
                Objects.requireNonNull(dc.getDocument().get("status")).toString(),
                Objects.requireNonNull(dc.getDocument().getDouble("valorabertura")).floatValue(),
                Objects.requireNonNull(dc.getDocument().getDouble("valorfechamento")).floatValue()
        );
    }
}