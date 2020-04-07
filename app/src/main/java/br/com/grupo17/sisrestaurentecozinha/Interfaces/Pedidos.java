package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Pedido;

public interface Pedidos {
    interface ModelPedido {
        void retrivePedidos();

        void updateStatus(String status, String idItem);
    }

    interface PedidoView extends InterfaceBase.ProgressVisibility {
        void updateListaRecycler();

        void onClickCardPedido(String status, String idItem);
    }

    interface PedidoPresenter {
        Context getContext();

        List<Pedido> getListPedidos();

        void retrievePedidos();

        void responseRetrieve(QuerySnapshot queryDocumentSnapshots);

        void responseUpdate(Task<Void> task);

        void removeItemRecycler(Pedido pedido);

        void sortList();

        void updateItemRecycler(Pedido pedido);

        void updateStatus(String status, String idItem);
    }
}