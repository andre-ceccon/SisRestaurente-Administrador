package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;
import br.com.grupo17.sisrestaurentecozinha.R;

public class CaixaPedidosAdapter  extends RecyclerView.Adapter<CaixaPedidosAdapter.PedidosViewHolder> {
    private  Context context;
    private Caixa.PedidosActivity view;
    private ArrayList<CaixaObj> pedidos;

    public CaixaPedidosAdapter(Context context, Caixa.PedidosActivity view, ArrayList<CaixaObj> pedidos) {
        this.context = context;
        this.view = view;
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public CaixaPedidosAdapter.PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CaixaPedidosAdapter.PedidosViewHolder(
                LayoutInflater.from((Context) view)
                        .inflate(R.layout.adapter_caixa_pedidos, parent, false)); //cria um novo objeto especificado o layout customizado que sera usado;
    }

    @Override
    public void onBindViewHolder(@NonNull CaixaPedidosAdapter.PedidosViewHolder holder, int position) {
        holder.setItem(pedidos.get(position)); //chama o metodo para setar o item;
        holder.setItemText(); //chama o metodo para setar as informações;
    }

    @Override
    public int getItemCount() {
        return pedidos.size(); //retorna a quantidade de itens que teremos no Recycler
    }

    class PedidosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CaixaObj item;
        private TextView nome;
        private TextView pratoNome;
        private TextView preco;

        PedidosViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tv_nome);
            pratoNome = itemView.findViewById(R.id.tv_pratonome);
            preco = itemView.findViewById(R.id.tv_preco);
        }

        void setItem(CaixaObj item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
        }

        //Metodo responsavel por setar as informações de cada item;
        void setItemText() {
            nome.setText(item.getNomeSobrenome());
            pratoNome.setText(item.getPratoNome());
            DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float
            preco.setText(context.getString(R.string.moeda) + " " + df.format(item.getPreco()));
        }
    }
}