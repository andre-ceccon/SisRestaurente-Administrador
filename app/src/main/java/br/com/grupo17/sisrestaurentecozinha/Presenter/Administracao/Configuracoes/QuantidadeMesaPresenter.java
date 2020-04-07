package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class QuantidadeMesaPresenter implements Administracao.QuantidadeMesaPresenter {
    private Administracao.QuantidadeMesaView view;
    private boolean reRetriveQtMesa, reUpdate;
    private String qtMesa;
    private ModelDb model;

    public QuantidadeMesaPresenter(Administracao.QuantidadeMesaView view) {
        this.view = view;
        this.reUpdate = false;
        this.reRetriveQtMesa = false;
        this.model = new ModelDb(this);
    }

    @Override
    public void alteraQuantidade(String quantidadeMesa) {
        if (TextUtils.isEmpty(quantidadeMesa)) {
            view.setErro(getContext().getString(R.string.erro_sem_numero));
        } else if (!Connectivity.isConnected(getContext())) {
            view.setErro(getContext().getString(R.string.erro_sem_conexao));
        } else {
            view.setProgressVisibility(true);
            view.setButtonEnabled(false);
            qtMesa = quantidadeMesa;
            model.updateQuantidadeMesa(quantidadeMesa);
        }
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
    public void buscaQuantidadeMesa() {
        view.setProgressVisibility(true);
        model.retriveQuantidadeMesa();
    }

    @Override
    public void responseRetriveQuantidade(Task<DocumentSnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetriveQtMesa) {
                reRetriveQtMesa = true;
                buscaQuantidadeMesa();
            } else {
                reRetriveQtMesa = false;
                view.setProgressVisibility(false);
                getExceptionTask(task, getContext().getString(R.string.erro_consulta_qt_mesa));
            }
        } else {
            if (task.getResult() != null) {
                reRetriveQtMesa = false;
                view.setProgressVisibility(false);
                try {
                    view.setTextQuantidade(Objects.requireNonNull(task.getResult().get(getContext().getString(R.string.key_campo_qtMesa_total))).toString());
                } catch (NullPointerException e) {
                    view.setTextQuantidade(getContext().getString(R.string.erro_pedido) + " " + task.getResult().getId());
                }
            }
        }
    }

    @Override
    public void responseUpdateQtMesa(Task<Void> task) {
        if (!task.isSuccessful()) {
            if (!reUpdate) {
                reUpdate = true;
                alteraQuantidade(qtMesa);
            } else {
                reUpdate = false;
                view.setProgressVisibility(false);
                view.setButtonEnabled(true);
                getExceptionTask(task, getContext().getString(R.string.erro_ao_atualizar_item));
            }
        } else {
            reUpdate = false;
            view.setButtonEnabled(true);
            view.setProgressVisibility(false);
            view.setTextQuantidade(qtMesa);
            view.limpaCampo();
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_update));
        }
    }
}