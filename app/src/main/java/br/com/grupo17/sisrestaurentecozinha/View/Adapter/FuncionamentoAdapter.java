package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.R;

public class FuncionamentoAdapter extends RecyclerView.Adapter<FuncionamentoAdapter.FuncionamentoAdapterViewHolder> {
    private Administracao.HorarioFuncionamentoView horarioNormal;
    private Administracao.HorarioEspecialView especial;
    private List<String> stringList;

    public FuncionamentoAdapter(Administracao.HorarioFuncionamentoView view, List<String> stringList) {
        this.horarioNormal = view;
        this.stringList = stringList;
    }

    public FuncionamentoAdapter(Administracao.HorarioEspecialView view, List<String> stringList) {
        this.especial = view;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public FuncionamentoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FuncionamentoAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_dia_semana_selecionado, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FuncionamentoAdapterViewHolder funcionamentoAdapterViewHolder, int i) {
        funcionamentoAdapterViewHolder.setText(stringList.get(i));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class FuncionamentoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView horario;

        FuncionamentoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            horario = itemView.findViewById(R.id.tv_horario);
            itemView.findViewById(R.id.deletehorario).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (horarioNormal != null) {
                horarioNormal.onClickDelete(horario.getText().toString());
            } else if (especial != null) {
                especial.onClickDelete(horario.getText().toString());
            }
        }

        void setText(String text) {
            horario.setText(text);
        }
    }
}