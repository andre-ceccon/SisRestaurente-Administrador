package br.com.grupo17.sisrestaurentecozinha.View.Caixa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa.EscolherMesaPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.MenuActivity;

public class EscolherMesaActivity extends AppCompatActivity implements Caixa.EscolherMesaActivity {
    private Caixa.EscolherMesaPresenter presenter;
    private Button continuar;
    private EditText mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_escolher_mesa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mesa = findViewById(R.id.et_mesa);
        continuar = findViewById(R.id.bt_fazer_pagamento);
        Button fechar = findViewById(R.id.bt_fechar_caixa);

        presenter = new EscolherMesaPresenter(this);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buscaMesa(mesa.getText().toString().trim());
            }
        });

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.alertFecharMesa();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EscolherMesaActivity.this, MenuActivity.class));
    }

    @Override
    public void mesaErros(int i) {
        switch (i) {
            case R.string.erro_campo_mesa_vazio:
                mesa.setError(getString(R.string.erro_campo_mesa_vazio));
                mesa.requestFocus();
                break;
            case R.string.erro_campo_mesa_invalida:
                mesa.setError(getString(R.string.erro_campo_mesa_invalida));
                mesa.requestFocus();
                break;
        }
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        continuar.setEnabled(enabled);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void alertFecharMesa(String titulo, String texto) {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setCancelable(false);

        mensagem.setPositiveButton(R.string.dialog_button_dialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.fecharCaixa();
            }
        }).setNegativeButton(R.string.dialog_button_dialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mensagem.show();
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(this, EscolherContasActivity.class));
    }

    @Override
    public void startActivityFecharCaixa() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}