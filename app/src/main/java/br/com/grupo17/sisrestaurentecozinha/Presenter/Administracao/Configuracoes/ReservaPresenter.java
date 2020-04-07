package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class ReservaPresenter implements Administracao.ReservaPresenter {
    private Administracao.ReservaView view;
    private boolean reRetrive, reUpdate;
    private Map<String, Object> map;
    private ModelDb model;

    public ReservaPresenter(Administracao.ReservaView view) {
        this.view = view;
        this.reUpdate = false;
        this.reRetrive = false;
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    private void getExceptionTask(Task task, String mensagem) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContext(), mensagem + ",\n" + e.getMessage());
        }
    }

    @Override
    public void retriveReserva() {
        view.setProgressVisibility(true);
        model.retriveReserva();
    }

    @Override
    public void responseReserva(Task<DocumentSnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetrive) {
                reRetrive = true;
                retriveReserva();
            } else {
                reRetrive = false;
                view.setProgressVisibility(false);
                getExceptionTask(task, getContext().getString(R.string.erro_consultar));
            }
        } else {
            if (task.getResult() != null) {
                reRetrive = false;
                view.setProgressVisibility(false);
                try {
                    view.setText(
                            Objects.requireNonNull(task.getResult().get("mesa")).toString(),
                            Objects.requireNonNull(task.getResult().get("pessoas")).toString()
                    );
                } catch (NullPointerException e) {
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_consultar));
                }
            }
        }
    }

    @Override
    public void alteraReserva(String mesa, String pessoas) {
        if (TextUtils.isEmpty(mesa)) {
            view.setErro(R.string.campo_mesa_reserva, getContext().getString(R.string.erro_sem_numero));
        } else if (TextUtils.isEmpty(pessoas)) {
            view.setErro(R.string.campo_pessoa_reserva, getContext().getString(R.string.erro_sem_numero));
        } else if (!Connectivity.isConnected(getContext())) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_sem_conexao));
        } else {
            map = new HashMap<>();
            map.put("mesa", mesa);
            map.put("pessoas", pessoas);
            view.setButtonEnabled(false);
            view.setProgressVisibility(true);
            model.updateReserva(map);
        }
    }

    @Override
    public void responseAlterar(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reUpdate && Connectivity.isConnected(getContext())) {
                reUpdate = true;
                model.updateReserva(map);
            } else {
                reUpdate = false;
                getExceptionTask(task, getContext().getString(R.string.erro_ao_salvar_item));
                view.setButtonEnabled(true);
                view.setProgressVisibility(false);
            }
        } else {
            reUpdate = false;
            view.limparCampos();
            view.setButtonEnabled(true);
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_cadastro));
        }
    }
}