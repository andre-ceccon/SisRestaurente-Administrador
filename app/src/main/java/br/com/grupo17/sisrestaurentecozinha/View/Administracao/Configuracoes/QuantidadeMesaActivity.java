package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.QuantidadeMesaPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class QuantidadeMesaActivity extends AppCompatActivity implements Administracao.QuantidadeMesaView {
    private Administracao.QuantidadeMesaPresenter presenter;
    private TextView quantidade_atual;
    private EditText quantidade;
    private Button alterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantidade_mesa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        quantidade_atual = findViewById(R.id.tx_mesa_atual);
        quantidade = findViewById(R.id.txt_qt_mesa);
        alterar = findViewById(R.id.bt_alterar);

        presenter = new QuantidadeMesaPresenter(this);
        presenter.buscaQuantidadeMesa();

        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(QuantidadeMesaActivity.this, alterar);
                presenter.alteraQuantidade(quantidade.getText().toString().trim());
            }
        });
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        alterar.setEnabled(enabled);
    }

    @Override
    public void setTextQuantidade(String quantidade) {
        quantidade_atual.setText(quantidade);
        alterar.setEnabled(true);
    }

    @Override
    public void setErro(String erro) {
        if (erro.equals(getString(R.string.erro_sem_conexao))) {
            SpAndToast.showMessage(this, getString(R.string.erro_sem_conexao));
        } else {
            quantidade.setError(erro);
            quantidade.requestFocus();
            Teclado.showKeyboard(this);
        }
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void limpaCampo() {
        quantidade.setText("");
        quantidade.clearFocus();
    }
}