package br.com.grupo17.sisrestaurentecozinha.View.Caixa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.ArrayList;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa.PedidosPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.CaixaPedidosAdapter;

public class PedidosActivity extends AppCompatActivity implements Caixa.PedidosActivity {
    private Caixa.PedidosPresenter presenter;
    private RecyclerView rv_Pedidos;
    private Button fazerPagamento;
    private CaixaPedidosAdapter adapter;
    private TextView valorTotal;
    private TextView totalItens;
    private CheckBox tirarDezPorcento;
    private ArrayList<CaixaObj> arrayList;
    private TextView cupomDesconto;
    private AlertDialog.Builder mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caixa_pedidos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        rv_Pedidos = findViewById(R.id.rv_pedidos);
        fazerPagamento = findViewById(R.id.bt_fechar_caixa);
        valorTotal = findViewById(R.id.tv_valor_total);
        totalItens = findViewById(R.id.tv_total_itens);
        tirarDezPorcento = findViewById(R.id.cb_tirar);
        cupomDesconto = findViewById(R.id.tv_cupom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new PedidosPresenter(this);

        if (getIntent().getExtras() != null) {
            arrayList = getIntent().getParcelableArrayListExtra("pedidos");
            presenter.setListPedidos(arrayList);
        }

        rv_Pedidos.setHasFixedSize(true);
        rv_Pedidos.setLayoutManager(new LinearLayoutManager(this));

        tirarDezPorcento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tirarDezPorcento.isChecked()) {
                    presenter.valorTotalSemDez();
                } else {
                    presenter.valorTotalComDez();
                }
            }
        });

        fazerPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.avisoPagamento();
            }
        });

        cupomDesconto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.alertCupom();
            }
        });

        criaLista();
    }

    @Override
    public void criaLista() {
        adapter = new CaixaPedidosAdapter(this, this, presenter.getListPedidos());
        rv_Pedidos.setAdapter(adapter);
        notificaLista();
        presenter.totalItens();
        presenter.valorTotalComDez();
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        fazerPagamento.setEnabled(enabled);
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
    public TextView totalItens() {
        return totalItens;
    }

    @Override
    public TextView valorTotal() {
        return valorTotal;
    }

    @Override
    public CheckBox checkBoxDezPorcento() {
        return tirarDezPorcento;
    }

    @Override
    public void alertCupom(String titulo, String texto) {
        mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);

        final EditText input = new EditText(this);
        mensagem.setView(input);
        mensagem.setNeutralButton(R.string.dialog_bt_aplicar_desconto, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                presenter.consultaCodigoDesconto(input.getText().toString().trim());
            }

        });

        mensagem.show();
    }

    @Override
    public void alertAviso(String titulo, String texto) {
        mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);

        mensagem.setCancelable(false);
        mensagem.setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.EscolherFormaPagamento();
            }
        }).setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mensagem.show();
    }

    @Override
    public void alertEscolherFormaPagamento(String titulo, final ArrayList<String> itens) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_forma_pagamento, itens);
        mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setCancelable(false);
        mensagem.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int x) {
                presenter.valorPago(itens.get(x));
                dialog.dismiss();
            }
        });
        mensagem.setNegativeButton(getString(R.string.dialog_button_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mensagem.show();
    }

    @Override
    public void alertValorPago(String titulo, String texto) {
        mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);

        mensagem.setCancelable(false);
        final EditText input = new EditText(this);
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setGravity(Gravity.CENTER);
        mensagem.setView(input);
        mensagem.setNeutralButton(R.string.dialog_bt_registrar_pagamento, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                presenter.cpf(input.getText().toString().trim());
            }

        });

        mensagem.setNegativeButton(getString(R.string.dialog_button_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mensagem.show();
    }

    @Override
    public void alertCpf(String titulo, String texto) {
        mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);

        mensagem.setCancelable(false);

        final EditText cpf = new EditText(this);

        cpf.setInputType(InputType.TYPE_CLASS_NUMBER);
        cpf.setGravity(Gravity.CENTER);

        SimpleMaskFormatter maskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN"); //Mascara cpf
        MaskTextWatcher mcpf = new MaskTextWatcher(cpf, maskcpf);

        cpf.addTextChangedListener(mcpf);

        mensagem.setView(cpf);
        mensagem.setNeutralButton(R.string.dialog_bt_finalizar, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                presenter.uidCaixa(cpf.getText().toString().replace(".", "").replace("-", ""));
            }

        });
        mensagem.show();
    }

    @Override
    public void startActivity(String valorPagoCliente, float valorTotal) {
        Intent intent = new Intent(this, PagamentoConcluidoActivity.class);
        intent.putExtra("valorpago", valorPagoCliente);
        intent.putExtra("valortotal", valorTotal);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}