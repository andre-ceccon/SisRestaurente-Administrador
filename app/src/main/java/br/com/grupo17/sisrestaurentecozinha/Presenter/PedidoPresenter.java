package br.com.grupo17.sisrestaurentecozinha.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Pedidos;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Pedido;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class PedidoPresenter implements Pedidos.PedidoPresenter {
    private List<Pedido> pedidos = new ArrayList<>();
    private Pedidos.PedidoView pedidoView;
    private ModelDb modelDb;

    public PedidoPresenter(Pedidos.PedidoView pedidoView) {
        this.pedidoView = pedidoView;
        this.modelDb = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) pedidoView;
    }

    @Override
    public List<Pedido> getListPedidos() {
        return pedidos;
    }

    @NonNull
    private String getString(int string) {
        return getContext().getString(string);
    }

    @Override
    public void removeItemRecycler(Pedido pedido) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId() != null && pedidos.get(i).getId().equals(pedido.getId())) {
                pedidos.remove(i);
                sortList();
                break;
            }
        }
    }

    @Override
    public void retrievePedidos() {
        pedidos.clear();
        pedidoView.setProgressVisibility(true);
        modelDb.retrivePedidos();
    }

    @Override
    public void responseRetrieve(QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null) {
            if (queryDocumentSnapshots.isEmpty()) {
                sortList();
            }

            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        pedidos.add(toObject(dc));
                        sortList();
                        break;
                    case MODIFIED:
                        if (Objects.equals(dc.getDocument().get(getContext().getString(R.string.key_campo_statusitem)), getContext().getString(R.string.key_status_cliente_pediu)) || Objects.equals(dc.getDocument().get(getContext().getString(R.string.key_campo_statusitem)), getContext().getString(R.string.key_status_cozinha_preparando))) {
                            updateItemRecycler(toObject(dc));
                        }
                        break;
                    case REMOVED:
                        removeItemRecycler(toObject(dc));
                        break;
                }
            }
        }

        pedidoView.setProgressVisibility(false);
    }

    @Override
    public void responseUpdate(Task<Void> task) {
        if (task.isSuccessful()) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_update));
        } else if (task.getException() != null) {
            try {
                throw task.getException();
            } catch (Exception e) {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_tente_novamente));
                SpAndToast.showMessage(getContext(), e.getMessage());
            }
        }
    }

    @Override
    public void sortList() {
        Collections.sort(pedidos, Pedido.dataComparator);
        pedidoView.updateListaRecycler();
    }

    private Pedido toObject(DocumentChange dc) {
        try {
            return new Pedido(
                    dc.getDocument().getId(),
                    dc.getDocument().getDate(getString(R.string.key_campo_data)),
                    Objects.requireNonNull(dc.getDocument().get(getString(R.string.key_campo_nome_prato))).toString(),
                    Objects.requireNonNull(dc.getDocument().get(getString(R.string.key_campo_ponto_carne))).toString(),
                    Objects.requireNonNull(dc.getDocument().get(getString(R.string.key_campo_observacao))).toString(),
                    Objects.requireNonNull(dc.getDocument().get(getString(R.string.key_campo_statusitem))).toString()
            );
        } catch (NullPointerException e) {
            return new Pedido(null, null, getString(R.string.erro_pedido) + "\n" + dc.getDocument().getId(), getString(R.string.erro_pedido_continuacao), null, null);
        }
    }

    @Override
    public void updateItemRecycler(Pedido pedido) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId() != null && pedidos.get(i).getId().equals(pedido.getId())) {
                pedidos.set(i, pedido);
                sortList();
                break;
            }
        }
    }

    @Override
    public void updateStatus(String status, String idItem) {
        modelDb.updateStatus(status, idItem);
    }
}