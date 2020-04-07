package br.com.grupo17.sisrestaurentecozinha.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Pedidos;
import br.com.grupo17.sisrestaurentecozinha.Presenter.PedidoPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.PedidosAdapter;

public class PedidosActivity extends AppCompatActivity implements Pedidos.PedidoView {
    private Pedidos.PedidoPresenter presenter;
    private PedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        presenter = new PedidoPresenter(this);
        presenter.retrievePedidos(); //busca pedidos
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView recyclerView = findViewById(R.id.rvPedidos);
        recyclerView.setHasFixedSize(true); //informando que os itens da lista não vão sofre alteração de tamanho.

        adapter = new PedidosAdapter(presenter.getListPedidos(), this);
        recyclerView.setAdapter(adapter); //setadando adapter na lista
    }

    /*
     * Método chamado quando um item da lista é clicado;
     * È criado um AlertDialog, perguntando se deseja realizar a operação de mudança de status do pedido.
     */
    @Override
    public void onClickCardPedido(final String status, final String idItem) {
        if (!TextUtils.isEmpty(status)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_pedido)
                    .setMessage(
                            status.equals(getString(R.string.key_status_cliente_pediu)) ? getString(R.string.dialog_mensagem_pedido) + " " + getString(R.string.key_status_cozinha_preparando) + "?" : getString(R.string.dialog_mensagem_pedido) + " " + getString(R.string.key_status_3) + "?"
                    )
                    .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.updateStatus(status.equals(getString(R.string.key_status_cliente_pediu)) ? getString(R.string.key_status_cozinha_preparando) : getString(R.string.key_status_3), idItem);
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else {
            SpAndToast.showMessage(this, getString(R.string.erro_pedido_continuacao));
        }
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /*
     * Método usado para informar ao adapter que houve alteração de dados na lista;
     * E caso a lista estiver vazia, é mostrado um texto falando que não a pedidos realizados
     */
    @Override
    public void updateListaRecycler() {
        if (adapter != null) {
            if (presenter.getListPedidos().size() > 0) {
                findViewById(R.id.tx_sem_pedidos).setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                TextView textView = findViewById(R.id.tx_sem_pedidos);
                textView.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.aviso_sem_pedido));
            }
        }
    }
}