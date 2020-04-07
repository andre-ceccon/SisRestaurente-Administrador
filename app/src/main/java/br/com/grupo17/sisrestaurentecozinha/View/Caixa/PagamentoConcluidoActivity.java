package br.com.grupo17.sisrestaurentecozinha.View.Caixa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa.PagamentoConcluidoPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;

public class PagamentoConcluidoActivity extends AppCompatActivity implements Caixa.PagamentoConcluidoActivity {
    private String valorPagoCliente;
    private Float valorTotal;
    private TextView valor;
    private TextView troco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_pagamento_concluido);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            valorPagoCliente = extras.getString("valorpago");
            valorTotal= extras.getFloat("valortotal");
        }

        valor = findViewById(R.id.tv_valor);
        troco = findViewById(R.id.tv_troco);
        Button voltar = findViewById(R.id.bt_voltar);

        Caixa.PagamentoConcluidoPresenter presenter = new PagamentoConcluidoPresenter(this);

        presenter.subtraiValores(valorPagoCliente,valorTotal);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntentMenu());
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(getIntentMenu());
        finish();
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public TextView valor() {
        return valor;
    }

    @Override
    public TextView troco() {
        return troco;
    }

    private Intent getIntentMenu(){
        Intent intent = new Intent(PagamentoConcluidoActivity.this, EscolherMesaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}