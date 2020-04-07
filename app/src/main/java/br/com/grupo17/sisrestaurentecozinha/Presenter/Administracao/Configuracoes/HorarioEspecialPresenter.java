package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class HorarioEspecialPresenter implements Administracao.HorarioEspecialPresenter {
    private boolean reRetrivehorario, reRetriveDia, reDelete, deletadoDia;
    private Administracao.HorarioEspecialView view;
    private ArrayList<String> dias, horarios;
    private String dia, horario;
    private ModelDb model;

    public HorarioEspecialPresenter(Administracao.HorarioEspecialView view) {
        this.view = view;
        this.reDelete = false;
        this.deletadoDia = false;
        this.reRetriveDia = false;
        this.reRetrivehorario = false;
        this.dias = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public void deleteHorario(String dia, String horario) {
        this.horario = horario;
        view.setProgressVisibility(true);
        view.setRecyclerVisibility(false);
        if (horarios.size() == 1) {
            deletadoDia = true;
            model.deleteHorario(dia, horario, true, true);
        } else {
            model.deleteHorario(dia, horario, true, false);
        }
    }

    @Override
    public void responseDelete(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reDelete) {
                reDelete = true;
                model.deleteHorario(dia, horario, true, horarios.size() == 0);
            } else {
                reDelete = false;
                view.setRecyclerVisibility(true);
                view.setProgressVisibility(false);
                getException(task, getContext().getString(R.string.erro_deletar_filme));
            }
        } else {
            reDelete = false;
            horarios.remove(horario);
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_deletar_sucesso));
            if (deletadoDia) {
                dias.remove(dia);
                view.setAdapterDias();
                view.setSubTitle(null);
                view.setProgressVisibility(false);
                if (dias.size() == 0) {
                    view.setInfoSemRegistro(true);
                } else {
                    view.setRecyclerVisibility(true);
                }
            } else {
                view.setAdapterHorario();
                view.setRecyclerVisibility(true);
                view.setProgressVisibility(false);
            }
        }
    }

    private void getException(Task task, String mensagem) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContext(), mensagem);
            SpAndToast.showMessage(getContext(), e.getMessage());
        }
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public ArrayList<String> getListDias() {
        return dias;
    }

    @Override
    public ArrayList<String> getListHorarios() {
        return horarios;
    }

    @Override
    public void retriveDiasEspeciais() {
        view.setProgressVisibility(true);
        model.retriveDiasEspeciais();
    }

    @Override
    public void retriveHorarios(String dia) {
        this.dia = dia;
        model.retriveHorarios(dia);
        view.setProgressVisibility(true);
        view.setRecyclerVisibility(false);
    }

    @Override
    public void responseDiasEspeciais(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetriveDia) {
                reRetriveDia = true;
                model.retriveDiasEspeciais();
            } else {
                reRetriveDia = false;
                view.setProgressVisibility(false);
                getException(task, getContext().getString(R.string.erro_consultar));
            }
        } else {
            if (task.getResult() != null) {
                view.setProgressVisibility(false);
                if (task.getResult().isEmpty()) {
                    view.setInfoSemRegistro(true);
                } else {
                    reRetriveDia = false;
                    dias.clear();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        dias.add(doc.getId());
                    }
                    view.setInfoSemRegistro(false);
                    view.setAdapterDias();
                }
            }
        }
    }

    @Override
    public void responseHorarios(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetrivehorario) {
                reRetrivehorario = true;
                model.retriveHorarios(dia);
            } else {
                reRetrivehorario = false;
                view.setProgressVisibility(false);
                view.setRecyclerVisibility(true);
                getException(task, getContext().getString(R.string.erro_consultar));
            }
        } else {
            if (task.getResult() != null) {
                view.setProgressVisibility(false);
                if (task.getResult().isEmpty()) {
                    view.setInfoSemRegistro(true);
                } else {
                    horarios.clear();
                    reRetrivehorario = false;
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        horarios.add(doc.getId());
                    }
                    view.setAdapterHorario();
                    view.setInfoSemRegistro(false);
                    view.setRecyclerVisibility(true);
                }
            }
        }
    }
}