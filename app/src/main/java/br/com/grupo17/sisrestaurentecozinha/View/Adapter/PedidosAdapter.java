package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Pedidos;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Pedido;
import br.com.grupo17.sisrestaurentecozinha.R;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidosViewHolder> {
    private Pedidos.PedidoView pedidoView;
    private List<Pedido> pedidos;

    public PedidosAdapter(List<Pedido> pedidos, Pedidos.PedidoView pedidoView) {
        this.pedidos = pedidos;
        this.pedidoView = pedidoView;
    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PedidosViewHolder(
                LayoutInflater.from((Context) pedidoView)
                        .inflate(R.layout.adapter_pedido, parent, false)); //cria um novo objeto especificado o layout customizado que sera usado;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosViewHolder holder, int position) {
        holder.setItem(pedidos.get(position)); //chama o metodo para setar o item;
        holder.setItemText(); //chama o metodo para setar as informações;
    }

    @Override
    public int getItemCount() {
        return pedidos.size(); //retorna a quantidade de itens que teremos no Recycler
    }

    class PedidosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pratonome, pontoCarne, status, observacao;
        private Pedido item;

        PedidosViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            pratonome = itemView.findViewById(R.id.pratonome);
            pontoCarne = itemView.findViewById(R.id.pontoCarne);
            status = itemView.findViewById(R.id.status);
            observacao = itemView.findViewById(R.id.observacao);
        }

        void setItem(Pedido item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            pedidoView.onClickCardPedido(item.getStatus(), item.getId());
        }

        //Metodo responsavel por setar as informações de cada item;
        void setItemText() {
            pratonome.setText(!TextUtils.isEmpty(item.getPratoNome()) ? item.getPratoNome() : "");
            pontoCarne.setText(!TextUtils.isEmpty(item.getPontoCarne()) ? item.getPontoCarne() : "");
            status.setText(!TextUtils.isEmpty(item.getStatus()) ? item.getStatus() : "");
            observacao.setText(!TextUtils.isEmpty(item.getObservacoes()) ? item.getObservacoes() : "");
        }
    }
}