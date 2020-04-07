package br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.Task;

import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class DetalheItemPresenter implements Cardapio.DetalhePresenter {
    private boolean reDeleteItem, reDeleteFoto;
    private Cardapio.DetalheView detalheView;
    private Cardapio.ModelCardapio model;
    private CardapioObj cardapioObj;

    public DetalheItemPresenter(Cardapio.DetalheView detalheView, CardapioObj cardapioObj) {
        this.reDeleteItem = false;
        this.reDeleteFoto = false;
        this.detalheView = detalheView;
        this.cardapioObj = cardapioObj;
        this.model = new ModelDb(this);
    }

    @Override
    public void deletaItem() {
        if (Connectivity.isConnected((Context) detalheView)) {
            detalheView.setProgressVisibility(R.string.key_delete, true);
            model.deletaItem(cardapioObj.getCategoria(), cardapioObj.getId());
        } else {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_sem_conexao));
        }
    }

    @Override
    public Context getContext() {
        return detalheView.getContext();
    }

    @Override
    public CardapioObj getItem() {
        return cardapioObj;
    }

    @Override
    public void responseDelete(int operacao, Task<Void> task) {
        switch (operacao) {
            case R.string.key_item:
                if (!task.isSuccessful()) {
                    if (!reDeleteItem) {
                        reDeleteItem = true;
                        model.deletaItem(cardapioObj.getCategoria(), cardapioObj.getId());
                    } else {
                        reDeleteItem = false;
                        getExceptionTask(task, getContext().getString(R.string.erro_pedido));
                    }
                } else {
                    reDeleteItem = false;
                    model.criaRefDeletaFotoComUrl(cardapioObj.getPathFoto());
                }
                break;
            case R.string.key_campo_foto:
                if (!task.isSuccessful()) {
                    if (!reDeleteFoto) {
                        reDeleteFoto = true;
                        model.criaRefDeletaFotoComUrl(cardapioObj.getPathFoto());
                    } else {
                        reDeleteFoto = false;
                        getExceptionTask(task, getContext().getString(R.string.erro_deletar_foto));
                        detalheView.finalizaDeleteItem();
                    }
                } else {
                    reDeleteFoto = false;
                    detalheView.finalizaDeleteItem();
                }
                break;
        }
    }

    private void getExceptionTask(Task task, String mensagem) {
        try {
            detalheView.setProgressVisibility(R.string.key_delete, false);
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(detalheView.getContext(), mensagem + ",\n" + e.getMessage());
        }
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            cardapioObj = data.getParcelableExtra(getContext().getString(R.string.key_item));
            detalheView.setTextInfo();
        }
    }
}