package br.com.grupo17.sisrestaurentecozinha.View.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.R;

public class CardapioSelecionouAdapter extends RecyclerView.Adapter<CardapioSelecionouAdapter.CardapioSelecionouAdapterViewHolder> {
    private Cardapio.CardapioView cardapioView;
    private List<CardapioObj> cardapioObjs;

    public CardapioSelecionouAdapter(List<CardapioObj> cardapioObjs, Cardapio.CardapioView cardapioView) {
        this.cardapioObjs = cardapioObjs;
        this.cardapioView = cardapioView;
    }

    @NonNull
    @Override
    public CardapioSelecionouAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CardapioSelecionouAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_categoria_selecionada, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CardapioSelecionouAdapterViewHolder cardapioSelecionouAdapterViewHolder, int i) {
        cardapioSelecionouAdapterViewHolder.setCardapioObj(cardapioObjs.get(i));
        cardapioSelecionouAdapterViewHolder.setText();
    }

    @Override
    public int getItemCount() {
        return cardapioObjs.size();
    }

    class CardapioSelecionouAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardapioObj cardapioObj;
        private TextView nome, status;
        private ProgressBar progress;
        private ImageView foto;

        CardapioSelecionouAdapterViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tx_nome);
            status = itemView.findViewById(R.id.tx_status);
            foto = itemView.findViewById(R.id.iv_foto);
            progress = itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(this);
        }

        void setCardapioObj(CardapioObj cardapioObj) {
            this.cardapioObj = cardapioObj;
        }

        void setText() {
            if (!TextUtils.isEmpty(cardapioObj.getPathFoto())) {
                progress.setVisibility(ProgressBar.VISIBLE);
                Picasso.get()
                        .load(cardapioObj.getPathFoto())
                        .fit()
                        .into(foto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress.setVisibility(ProgressBar.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                progress.setVisibility(ProgressBar.GONE);
                                Toast.makeText((Context) cardapioView, R.string.erro_baixar_imagem, Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            nome.setText(cardapioObj.getNome());
            status.setText(cardapioObj.getStatus());
        }

        @Override
        public void onClick(View v) {
            cardapioView.onClickSelecionouItem(cardapioObj);
        }
    }
}