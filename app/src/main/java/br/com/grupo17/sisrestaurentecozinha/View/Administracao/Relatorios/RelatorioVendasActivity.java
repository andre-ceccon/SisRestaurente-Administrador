package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Relatorios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Relatorios.RelatorioVendasVendasPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class RelatorioVendasActivity extends AppCompatActivity implements Administracao.RelatoriosVendasView {
    private TextView data, pedido, preco, nome_mais, nome_menos, quantidadeMais, quantidadeMenos;
    private Administracao.RelatoriosVendasPresenter presenter;
    private EditText dias;
    private Button gerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatoriovendas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        dias = findViewById(R.id.tx_dias);
        data = findViewById(R.id.txt_data);
        preco = findViewById(R.id.txt_somotorio_preco);
        pedido = findViewById(R.id.txt_somatorio_pedido);
        nome_mais = findViewById(R.id.txt_mais_pedido);
        nome_menos = findViewById(R.id.txt_menos_pedido);
        quantidadeMais = findViewById(R.id.txt_qt_mais_pedido);
        quantidadeMenos = findViewById(R.id.txt_qt_menos_pedido);

        presenter = new RelatorioVendasVendasPresenter(this);

        gerar = findViewById(R.id.bt_gerar);
        gerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(RelatorioVendasActivity.this, gerar);
                presenter.validacaoDados(dias.getText().toString().trim());
            }
        });
    }

    /*
     * Método usado habilitar ou desablitar o click do botão
     */
    @Override
    public void setButtonEnabled(boolean enabled) {
        gerar.setEnabled(enabled);
    }

    /*
     * Método usado para apresentar a mensagem de erro;
     */
    @Override
    public void setErro(String mensagem) {
        dias.setError(mensagem);
        dias.requestFocus();
        Teclado.showKeyboard(this);
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /*
     * Método usado para setar o texto do relatorio
     */
    @Override
    public void setText(Map<String, Object> map) {
        if (map != null) {
            dias.setText(null);
            dias.clearFocus();

            data.setText(Objects.requireNonNull(map.get(getString(R.string.key_campo_data))).toString());

            //formata o preco para no maximo duas casas decimais;
            BigDecimal aNumber = new BigDecimal(Float.valueOf(Objects.requireNonNull(map.get(getString(R.string.key_campo_preco))).toString()));
            preco.setText(String.valueOf(aNumber.setScale(2, BigDecimal.ROUND_HALF_UP)));

            pedido.setText(Objects.requireNonNull(map.get(getString(R.string.key_relatorioVendas_quantidade_pedido))).toString());

            nome_menos.setText(
                    TextUtils.isEmpty(Objects.requireNonNull(map.get(getString(R.string.key_relatorioVendas_nome_menos))).toString()) ? getString(R.string.aviso_sem_item_menos_pedidos) : Objects.requireNonNull(map.get(getString(R.string.key_relatorioVendas_nome_menos))).toString()
            );

            nome_mais.setText(
                    Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_nome_mais)))).equals(Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_nome_menos))))) ?
                            getString(R.string.aviso_sem_item_mais_pedidos) : Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_nome_mais))))
            );

            quantidadeMais.setText(
                    Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_quantidade_mais)))).equals(Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_quantidade_mais))))) ?
                            Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_quantidade_mais)))) : "0"
            );

            quantidadeMenos.setText(Objects.requireNonNull(String.valueOf(map.get(getString(R.string.key_relatorioVendas_quantidade_menos)))));
        }
    }
}