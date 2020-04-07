package br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class EscolherMesaPresenter implements Caixa.EscolherMesaPresenter {
    private Caixa.EscolherMesaActivity view;
    private Caixa.ModelCaixa model;

    public EscolherMesaPresenter(Caixa.EscolherMesaActivity view) {
        this.view = view;
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void buscaMesa(String mesa) {
        if (TextUtils.isEmpty(mesa)) {
            view.mesaErros(R.string.erro_campo_mesa_vazio);
        } else {
            view.setButtonEnabled(false);
            view.setProgressVisibility(true);
            model.consultaMesa(mesa);
        }
    }

    @Override
    public void responseConsultaMesa(Task<QuerySnapshot> task, String mesa) {
        view.setProgressVisibility(false);
        view.setButtonEnabled(true);
        if (task.isSuccessful()) {
            if (task.getResult() != null) {
                if (!task.getResult().isEmpty()) {
                    SpAndToast.saveSP(getContext(), "mesa", mesa);
                    view.startActivity();
                } else {
                    view.mesaErros(R.string.erro_campo_mesa_invalida);
                }
            }
        } else {
            try {
                SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), getContext().getString(R.string.erro_consultar)).getMessage());
            } catch (NullPointerException e) {
                SpAndToast.showMessage(getContext(), e.getMessage());
            }
        }
    }

    @Override
    public void alertFecharMesa() {
        view.alertFecharMesa(getContext().getString(R.string.dialog_caixa_fechar_titulo), getContext().getString(R.string.dialog_caixa_fechar_mensagem));
    }

    @Override
    public void fecharCaixa() {
        model.fecharCaixa();
    }

    @Override
    public void responseFecharCaixa(Task<Void> task) {
        if (task.isSuccessful()) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.caixa_fechado));
            view.startActivityFecharCaixa();
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), "Erro ao fechar caixa").getMessage());
        }
    }
}