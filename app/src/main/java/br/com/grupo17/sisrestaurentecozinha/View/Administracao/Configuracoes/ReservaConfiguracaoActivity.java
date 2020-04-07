package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.ReservaPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class ReservaConfiguracaoActivity extends AppCompatActivity implements Administracao.ReservaView {
    private Administracao.ReservaPresenter presenter;
    private TextView tv_mesa, tv_pessoa;
    private EditText et_mesa, et_pessoa;
    private Button alterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_configuracao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        presenter = new ReservaPresenter(this);

        tv_mesa = findViewById(R.id.tv_numero_mesa_reserva);
        tv_pessoa = findViewById(R.id.tv_pessoa_mesa);
        et_mesa = findViewById(R.id.txt_numero_mesa);
        et_pessoa = findViewById(R.id.txt_pessoas);
        alterar = findViewById(R.id.bt_alterar);

        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    Teclado.hideKeyboard(ReservaConfiguracaoActivity.this, getCurrentFocus());
                }
                presenter.alteraReserva(et_mesa.getText().toString().trim(), et_pessoa.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.retriveReserva();
    }

    @Override
    public void setText(String mesa, String pessoas) {
        tv_mesa.setText(mesa);
        tv_pessoa.setText(pessoas);
    }

    @Override
    public void setErro(int campo, String menssgem) {
        Teclado.showKeyboard(this);
        switch (campo) {
            case R.string.campo_mesa_reserva:
                et_mesa.setError(menssgem);
                et_mesa.requestFocus();
                break;
            case R.string.campo_pessoa_reserva:
                et_pessoa.setError(menssgem);
                et_pessoa.requestFocus();
                break;
        }
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        alterar.setEnabled(enabled);
    }

    @Override
    public void limparCampos() {
        tv_mesa.setText(et_mesa.getText().toString().trim());
        tv_pessoa.setText(et_pessoa.getText().toString().trim());
        et_mesa.setText("");
        et_pessoa.setText("");
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        }
    }
}