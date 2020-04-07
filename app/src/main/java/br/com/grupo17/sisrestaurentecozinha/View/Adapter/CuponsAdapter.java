package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Cupom;
import br.com.grupo17.sisrestaurentecozinha.R;

public class CuponsAdapter extends RecyclerView.Adapter<CuponsAdapter.CuponsAdaptersAdapterViewHolder> {
    private Administracao.GerenciamentoCupomView view;
    private List<Cupom> cupomList;

    public CuponsAdapter(Administracao.GerenciamentoCupomView view, List<Cupom> cupomList) {
        this.view = view;
        this.cupomList = cupomList;
    }

    @NonNull
    @Override
    public CuponsAdaptersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CuponsAdaptersAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_list_cupons, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CuponsAdaptersAdapterViewHolder cuponsAdaptersAdapterViewHolder, int i) {
        cuponsAdaptersAdapterViewHolder.setCupom(cupomList.get(i));
        cuponsAdaptersAdapterViewHolder.setText();
    }

    @Override
    public int getItemCount() {
        return cupomList.size();
    }

    class CuponsAdaptersAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView codigo, quantidade, limite, desconto;
        private Cupom cupom;

        CuponsAdaptersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            limite = itemView.findViewById(R.id.tx_usado);
            codigo = itemView.findViewById(R.id.tx_codigo);
            desconto = itemView.findViewById(R.id.tx_desconto);
            quantidade = itemView.findViewById(R.id.tx_quantidade);

            itemView.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onClick(cupom);
                }
            });
        }

        void setCupom(Cupom cupom) {
            this.cupom = cupom;
        }

        void setText() {
            codigo.setText(cupom.getId());
            quantidade.setText(cupom.getQuantidade() == -1 ? "Ilimitado": String.valueOf(cupom.getQuantidade()));

            BigDecimal aNumber = new BigDecimal(cupom.getDesconto());
            aNumber = aNumber.setScale(2, BigDecimal.ROUND_HALF_UP);
            desconto.setText(String.valueOf(aNumber));

            if (cupom.getQuantidade() == -1) {
                limite.setText(String.valueOf(cupom.getLimite()));
            } else {
                aNumber = new BigDecimal(((float) cupom.getLimite() / (float) cupom.getQuantidade()) * 100);
                aNumber = aNumber.setScale(2, BigDecimal.ROUND_HALF_UP);
                String as = cupom.getLimite() + " (" + (aNumber) + "%)";
                limite.setText(as);
            }
        }
    }
}