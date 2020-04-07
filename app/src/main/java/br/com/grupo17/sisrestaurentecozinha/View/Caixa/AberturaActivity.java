package br.com.grupo17.sisrestaurentecozinha.View.Caixa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa.AberturaPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.MenuActivity;

public class AberturaActivity extends AppCompatActivity implements Caixa.AberturaActivity {
    private Caixa.AberturaPresenter presenter;
    private EditText valor;
    private Button abrir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_abertura);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        valor = findViewById(R.id.et_valor_abertura);
        abrir = findViewById(R.id.bt_abrir_caixa);

        presenter = new AberturaPresenter(this);
        presenter.verificaCaixaAberto();

        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.aberturaCaixa(valor.getText().toString());
            }
        });
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(AberturaActivity.this, EscolherMesaActivity.class));
        finish();
    }

    @Override
    public void mesaErros(int i) {
        if (i == R.string.erro_caixa_abertura_vazio) {
            valor.setError(getString(R.string.erro_caixa_abertura_vazio));
            valor.requestFocus();
        }
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        abrir.setEnabled(enabled);
    }

    @Override
    public void startActivityMenu() {
        startActivity(new Intent(AberturaActivity.this, MenuActivity.class));
        finish();
    }
}