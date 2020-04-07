package br.com.grupo17.sisrestaurentecozinha.View.Conta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Conta;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Conta.RecuperaSenhaPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class RecuperarSenhaActivity extends AppCompatActivity implements Conta.RecuperaSenhaView {
    private Conta.RecuperaSenhaPresenter presenter;
    private Button recuperar;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_senha);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        email = findViewById(R.id.email);
        recuperar = findViewById(R.id.bt_recuperar);

        presenter = new RecuperaSenhaPresenter(this);
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(RecuperarSenhaActivity.this, recuperar);
                presenter.sendRecuperar(email.getText().toString().trim());
            }
        });
    }

    /*
     * Método usado habilitar ou desablitar o click do botão
     */
    @Override
    public void setButtonEnabled(boolean enabled) {
        recuperar.setEnabled(enabled);
    }

    /*
     * Método usado para apresentar a mensagem de erro;
     * */
    @Override
    public void setError(String mensagem) {
        email.setError(mensagem);
        email.requestFocus();
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /*
     * Método usado para abrir a tela de Login e fechar a tela atual;
     */
    @Override
    public void sucessoRecuperar() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}