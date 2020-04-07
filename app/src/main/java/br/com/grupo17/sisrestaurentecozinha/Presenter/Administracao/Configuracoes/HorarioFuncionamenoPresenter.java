package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class HorarioFuncionamenoPresenter implements Administracao.HorarioFuncionamentoPresenter {
    private List<String> horariosCadastrados;
    private Administracao.HorarioFuncionamentoView view;
    private boolean reConsulta, reDelete;
    private String dia, horario;
    private ModelDb model;

    public HorarioFuncionamenoPresenter(Administracao.HorarioFuncionamentoView view) {
        this.view = view;
        this.reDelete = false;
        this.reConsulta = false;
        this.horariosCadastrados = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public void deletarHorario(String dia, String horario) {
        this.dia = dia;
        this.horario = horario;
        view.setProgressVisibility(true);
        view.setRecyclerVisibility(false);
        model.deleteHorario(dia, horario, false, false);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    private void getException(Task task) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_consultar));
        }
    }

    @Override
    public List<String> getHorariosCadastrados() {
        return horariosCadastrados;
    }

    @Override
    public void retriveHorarios(String dia) {
        this.dia = dia;
        view.setSubTitle(dia);
        horariosCadastrados.clear();
        view.setProgressVisibility(true);
        view.setRecyclerVisibility(false);
        model.retriveHorariosFuncionamentos(dia);
    }

    @Override
    public void responseHorarios(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reConsulta) {
                reConsulta = true;
                model.retriveHorariosFuncionamentos(dia);
            } else {
                reConsulta = false;
                view.setSubTitle(null);
                view.setRecyclerVisibility(true);
                view.setProgressVisibility(false);
                getException(task);
            }
        } else {
            if (task.getResult() != null) {
                reConsulta = false;
                if (task.getResult().isEmpty()) {
                    view.setInfoSemRegistro(true);
                    view.setProgressVisibility(false);
                    view.setVissibilityButtom(true);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        horariosCadastrados.add(document.getId());
                    }
                    view.updateListComHorarios();
                    view.setInfoSemRegistro(false);
                    view.setRecyclerVisibility(true);
                    view.setProgressVisibility(false);
                }
            }
        }
    }

    @Override
    public void responseDelete(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reDelete) {
                reDelete = true;
                model.deleteHorario(dia, horario, false, false);
            } else {
                reDelete = false;
                view.setRecyclerVisibility(true);
                view.setProgressVisibility(false);
                getException(task);
            }
        } else {
            reDelete = false;
            view.setRecyclerVisibility(true);
            view.setProgressVisibility(false);
            horariosCadastrados.remove(horario);
            view.updateListComHorarios();
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_deletar_sucesso));
            if (horariosCadastrados.size() == 0) {
                view.setInfoSemRegistro(true);
            }
        }
    }
}