package br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa;

import android.content.Context;

import java.text.DecimalFormat;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.R;

public class PagamentoConcluidoPresenter implements Caixa.PagamentoConcluidoPresenter {
    private Caixa.PagamentoConcluidoActivity view;

    public PagamentoConcluidoPresenter(Caixa.PagamentoConcluidoActivity view) {
        this.view = view;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void subtraiValores(String valorPagoCliente, Float valorTotal) {
        DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float

        view.valor().setText(getContext().getText(R.string.caixa_valor_total) + df.format(valorTotal));

        Float troco = Float.parseFloat(valorPagoCliente.replace(",", ".")) - valorTotal;

        view.troco().setText(getContext().getText(R.string.caixa_valor_troco) + df.format(troco));
    }
}