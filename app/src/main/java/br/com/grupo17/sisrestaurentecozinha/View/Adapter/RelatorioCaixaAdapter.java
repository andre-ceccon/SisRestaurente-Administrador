package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaRelatorio;
import br.com.grupo17.sisrestaurentecozinha.R;

public class RelatorioCaixaAdapter extends RecyclerView.Adapter<RelatorioCaixaAdapter.RelatorioCaixaAdapterViewHolder> {
    private Administracao.CaixaRelatorioView view;
    private List<CaixaRelatorio> list;

    public RelatorioCaixaAdapter(Administracao.CaixaRelatorioView view, List<CaixaRelatorio> list) {
        this.view = view;
        this.list = list;
    }

    @NonNull
    @Override
    public RelatorioCaixaAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new RelatorioCaixaAdapterViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_relatorio_caixa, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioCaixaAdapterViewHolder relatorioCaixaAdapterViewHolder, int i) {
        relatorioCaixaAdapterViewHolder.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RelatorioCaixaAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CaixaRelatorio relatorio;
        private TextView nome, status;

        RelatorioCaixaAdapterViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tx_nome);
            status = itemView.findViewById(R.id.tx_status);
            itemView.setOnClickListener(this);
        }

        private void setText(CaixaRelatorio relatorio) {
            this.relatorio = relatorio;
            nome.setText(relatorio.getNome());
            status.setText(relatorio.getStatus());
        }

        @Override
        public void onClick(View v) {
            view.onClick(relatorio);
        }
    }
}