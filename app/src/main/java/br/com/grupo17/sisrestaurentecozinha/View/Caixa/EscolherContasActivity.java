package br.com.grupo17.sisrestaurentecozinha.View.Caixa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa.EscolherContasPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.ClientesAdapter;

public class EscolherContasActivity extends AppCompatActivity implements Caixa.EscolherContasActivity {
    private Caixa.EscolherContasPresenter presenter;
    private ClientesAdapter adapter;
    private RecyclerView lvContas;
    private Button continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_escolher_contas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvContas = findViewById(R.id.lv_contas);
        continuar = findViewById(R.id.bt_fechar_caixa);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buscaPedidos();
            }
        });

        lvContas.setHasFixedSize(true);
        lvContas.setLayoutManager(new LinearLayoutManager(this));

        presenter = new EscolherContasPresenter(this);
        presenter.buscaNomesContas();
    }

    @Override
    public void criaLista() {
        adapter = new ClientesAdapter(presenter.getListClientes(), presenter.getListClientesUIDS(), this);
        lvContas.setAdapter(adapter);
        notificaLista();
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
    public void notificaLista() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void startActivity() {
        Intent intent = new Intent(this, PedidosActivity.class);
        intent.putParcelableArrayListExtra("pedidos", presenter.getListPedidos());
        startActivity(intent);
    }
}