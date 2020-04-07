package br.com.grupo17.sisrestaurentecozinha.View.Conta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Conta;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Conta.LoginPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;
import br.com.grupo17.sisrestaurentecozinha.View.MenuActivity;

public class LoginActivity extends AppCompatActivity implements Conta.LoginView {
    private Conta.LoginPresenter loginPresenter;
    private EditText email, password;
    private TextView recupera;
    private Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        entrar = findViewById(R.id.entrar);
        password = findViewById(R.id.senha);
        recupera = findViewById(R.id.recuperar);

        loginPresenter = new LoginPresenter(this);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(LoginActivity.this, entrar);
                loginPresenter.onSendLogin(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        recupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(LoginActivity.this, recupera);
                startActivity(new Intent(LoginActivity.this, RecuperarSenhaActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.checkLogin(); //verifica se está logado
    }

    @Override
    public void isLogin(boolean isLogin) {
        if (isLogin) {
            loginSuccess(); //Caso já tenha uma conta logada, irá abrir a tela de Menu, se não a tela de login mesmo;
        }
    }

    /*
     * Método usado para apresentar a mensagem de erro;
     * */
    @Override
    public void loginError(int i) {
        switch (i) {
            case R.string.erro_campo_vazio:
                SpAndToast.showMessage(this, getString(R.string.erro_campo_vazio));
                break;
            case R.string.erro_email_invalido:
                email.setError(getString(R.string.erro_email_invalido)); //Seta no campo essa mensagem de erro;
                email.requestFocus(); //Coloca o foco no campo
                break;
            case R.string.erro_senha_errado:
                password.setError(getString(R.string.erro_senha_errado));
                password.requestFocus();
                break;
            case R.string.erro_email_nao_cadastrado:
                email.setError(getString(R.string.erro_email_nao_cadastrado));
                email.requestFocus();
                break;
            case R.string.erro_sem_conexao:
                SpAndToast.showMessage(this, getString(R.string.erro_sem_conexao));
                break;
            case R.string.erro_nivel_usuario:
                SpAndToast.showMessage(this, getString(R.string.erro_nivel_usuario));
                break;
            default:
                SpAndToast.showMessage(this, getString(R.string.erro_login));
                break;
        }
    }

    /*
    * Método usado para abrir a tela de Menu e fechar a tela de login;
    */
    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    /*
     * Método usado habilitar ou desablitar o click do botão
     */
    @Override
    public void setButtonEnabled(boolean enabled) {
        entrar.setEnabled(enabled);
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}