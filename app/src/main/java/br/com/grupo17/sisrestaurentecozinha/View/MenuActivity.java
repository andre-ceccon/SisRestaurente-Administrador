package br.com.grupo17.sisrestaurentecozinha.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.MenuInter;
import br.com.grupo17.sisrestaurentecozinha.Presenter.MenuPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes.GerenciamentoCupomActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes.HorarioFuncionamentoViewActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes.QuantidadeMesaActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes.ReservaConfiguracaoActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes.UsuarioActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Relatorios.AberturaFechamentoCaixaActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Administracao.Relatorios.RelatorioVendasActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Caixa.AberturaActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Cardapio.GerenciadorCardapioActivity;
import br.com.grupo17.sisrestaurentecozinha.View.Conta.LoginActivity;

public class MenuActivity extends AppCompatActivity implements MenuInter.MenuView {
    private MenuPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        presenter = new MenuPresenter(this);
        presenter.nivelUserRealTime(); //adiciona um listener para a mudança de nivel do usuario logado

        String niveluser = SpAndToast.getSP(this, getString(R.string.key_campo_nivelUser)); //Recupera o nivel do usuario salvo no sharedPreferences

        Button caixa = findViewById(R.id.bt_caixa);
        Button pedidos = findViewById(R.id.bt_pedidos);
        Button cardapio = findViewById(R.id.bt_cardapio);

        /*
        * Visibilidades e click dos botoes: Pedido, Cardapio e Caixa, variam dependendo do nivel do usuario logado;
        * atraves dos 3 if's/else's abaixo
        */
        if (niveluser.equals(getString(R.string.key_nivelUser_adm)) || niveluser.equals(getString(R.string.key_nivelUser_cozinha))) {
            pedidos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuActivity.this, PedidosActivity.class));
                }
            });
            pedidos.setVisibility(View.VISIBLE);
        } else {
            pedidos.setVisibility(View.GONE);
        }

        if (niveluser.equals(getString(R.string.key_nivelUser_adm)) || niveluser.equals(getString(R.string.key_nivelUser_cozinha))) {
            cardapio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuActivity.this, GerenciadorCardapioActivity.class));
                }
            });
            cardapio.setVisibility(View.VISIBLE);
        } else {
            cardapio.setVisibility(View.GONE);
        }

        if (niveluser.equals(getString(R.string.key_nivelUser_adm)) || niveluser.equals(getString(R.string.key_nivelUser_caixa))) {
            caixa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuActivity.this, AberturaActivity.class));
                }
            });
            caixa.setVisibility(View.VISIBLE);
        } else {
            caixa.setVisibility(View.GONE);
        }

        //botão para deslogar-se do aplicativo;
        findViewById(R.id.bt_sair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              fechaActivity("Deslogado com sucesso");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //O menu só sera criado quando o usuario logar-se já com o nivel de administrador
        if (SpAndToast.getSP(this, getString(R.string.key_campo_nivelUser)).equals(getString(R.string.key_nivelUser_adm))) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    //Método é chamado quando é realizado um click no menu. E abre a tela correspondente ao item clicado;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_vendas:
                startActivity(new Intent(MenuActivity.this, RelatorioVendasActivity.class));
                return true;
            case R.id.id_descontos:
                Intent d = new Intent(MenuActivity.this, GerenciamentoCupomActivity.class);
                d.putExtra("tela", "desconto");
                startActivity(d);
                return true;
            case R.id.quantidades_mesa:
                startActivity(new Intent(MenuActivity.this, QuantidadeMesaActivity.class));
                return true;
            case R.id.reservas:
                startActivity(new Intent(MenuActivity.this, ReservaConfiguracaoActivity.class));
                return true;
            case R.id.horario_funcionameno:
                startActivity(new Intent(MenuActivity.this, HorarioFuncionamentoViewActivity.class));
                return true;
            case R.id.controle_user:
                startActivity(new Intent(MenuActivity.this, UsuarioActivity.class));
                return true;
            case R.id.gerenciaento_cupom:
                Intent c = new Intent(MenuActivity.this, GerenciamentoCupomActivity.class);
                c.putExtra("tela", "cadastro");
                startActivity(c);
                return true;
            case R.id.id_caixa:
                startActivity(new Intent(MenuActivity.this, AberturaFechamentoCaixaActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void fechaActivity(String mgs) {
        presenter.deslogar();
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        SpAndToast.showMessage(MenuActivity.this, mgs);
        finish();
    }
}