package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.R;

public class ArrayStringAdapter extends RecyclerView.Adapter<ArrayStringAdapter.CardapioCategoriasAdapterViewHolder> {
    private Administracao.HorarioEspecialView horarioEspecialView;
    private Administracao.HorarioFuncionamentoView horarioView;
    private Cardapio.CardapioView cardapioView;
    private ArrayList<String> strings;

    public ArrayStringAdapter(ArrayList<String> strings, Cardapio.CardapioView cardapioView) {
        this.strings = strings;
        this.cardapioView = cardapioView;
    }

    public ArrayStringAdapter(ArrayList<String> strings, Administracao.HorarioFuncionamentoView horarioView) {
        this.horarioView = horarioView;
        this.strings = strings;
    }

    public ArrayStringAdapter(ArrayList<String> strings, Administracao.HorarioEspecialView horarioEspecialView) {
        this.horarioEspecialView = horarioEspecialView;
        this.strings = strings;
    }

    @NonNull
    @Override
    public CardapioCategoriasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardapioCategoriasAdapterViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_array_string, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CardapioCategoriasAdapterViewHolder holder, int position) {
        holder.text.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    class CardapioCategoriasAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text;

        CardapioCategoriasAdapterViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.item_categ);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (cardapioView != null) {
                cardapioView.onClickCardapioCategoria(text.getText().toString());
            } else if (horarioView != null) {
                horarioView.onClickDiaSemana(text.getText().toString());
            } else if (horarioEspecialView != null) {
                horarioEspecialView.onClickDia(text.getText().toString());
            }
        }
    }
}