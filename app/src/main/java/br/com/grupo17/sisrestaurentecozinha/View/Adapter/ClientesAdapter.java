package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Caixa.EscolherContasActivity;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder> {
    private Caixa.EscolherContasActivity view;
    private List<CaixaObj> ListClientes;
    private List<String> clientesUIDS;

    public ClientesAdapter(List<CaixaObj> ListClientes, List<String> clientesUIDS, EscolherContasActivity view) {
        this.view = view;
        this.clientesUIDS = clientesUIDS;
        this.ListClientes = ListClientes;
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientesViewHolder(
                LayoutInflater.from((Context) view)
                        .inflate(R.layout.adapter_caixa_clientes, parent, false)); //cria um novo objeto especificado o layout customizado que sera usado;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesViewHolder holder, int position) {
        holder.setItem(ListClientes.get(position)); //chama o metodo para setar o item;
        holder.setItemText(); //chama o metodo para setar as informações;
    }

    @Override
    public int getItemCount() {
        return ListClientes.size(); //retorna a quantidade de itens que teremos no Recycler
    }

    class ClientesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox clientes;
        private CaixaObj item;

        ClientesViewHolder(View itemView) {
            super(itemView);
            clientes = itemView.findViewById(R.id.cb_cliente);
            clientes.setOnClickListener(this);
        }

        void setItem(CaixaObj item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (clientes.isChecked()) {
                if (!clientesUIDS.contains(item.getUidUser())) {
                    clientesUIDS.add(item.getUidUser());
                }
            } else {
                clientesUIDS.remove(item.getUidUser());
            }
        }

        //Metodo responsavel por setar as informações de cada item;
        void setItemText() {
            clientes.setText(item.getNomeSobrenome());
        }
    }
}