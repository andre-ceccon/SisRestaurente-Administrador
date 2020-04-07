package br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class CardapioPresenter implements Cardapio.CardapioPresenter {
    private Cardapio.ModelCardapio model;
    private Cardapio.CardapioView view;
    private List<CardapioObj> itens;

    public CardapioPresenter(Cardapio.CardapioView view) {
        this.view = view;
        this.itens = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public void buscarItens(String categoria) {
        itens.clear();
        view.setProgressVisibility(true);
        view.setSubTitle(categoria);
        model.buscarItens(categoria);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public List<CardapioObj> getList() {
        return itens;
    }

    @Override
    public void responseBusca(QuerySnapshot queryDocumentSnapshots) {
        view.setProgressVisibility(false);
        if (queryDocumentSnapshots.isEmpty()) {
            view.setInfoSemRegistro();
        } else {
            for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                itens.add(toObject(dc));
                view.updateListaRecycler();
            }
        }
    }

    @Override
    public void setErro(Exception e) {
        SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_consultar) + "\n" + e.getMessage());
    }

    private CardapioObj toObject(DocumentSnapshot dc) {
        try {
            return new CardapioObj(
                    dc.getId(),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_foto))).toString(),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_nome_prato))).toString(),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_statusitem))).toString(),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_descricao))).toString(),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_categoria).toLowerCase())).toString(),
                    Objects.requireNonNull(dc.getDouble(getContext().getString(R.string.key_campo_avaliacao))).floatValue(),
                    Objects.requireNonNull(dc.getDouble(getContext().getString(R.string.key_campo_preco))).floatValue(),
                    Objects.requireNonNull(dc.getDouble(getContext().getString(R.string.key_campo_quantidade_avaliacao))).floatValue()
            );
        } catch (NullPointerException e) {
            return new CardapioObj(null, null, getContext().getString(R.string.erro_pedido) + "\n" + dc.getId(), null, null, null, null, null);
        }
    }
}