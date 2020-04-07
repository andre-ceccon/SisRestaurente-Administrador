package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Cupom;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.GerenciamentoCupomPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.CuponsAdapter;

public class GerenciamentoCupomActivity extends AppCompatActivity implements Administracao.GerenciamentoCupomView {
    private Administracao.GerenciamentoCupomPresenter presenter;
    private EditText codigo, quantidade, valor;
    private RecyclerView recyclerView;
    private ConstraintLayout cad;
    private CuponsAdapter adapter;
    private CheckBox ilimidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciamento_cupom);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cad = findViewById(R.id.cl_cad);
        valor = findViewById(R.id.tx_valor);
        codigo = findViewById(R.id.tx_codigo);
        quantidade = findViewById(R.id.tx_qt);
        ilimidado = findViewById(R.id.cb_ilimitado);
        recyclerView = findViewById(R.id.rv_cupons);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        presenter = new GerenciamentoCupomPresenter(this);
        findViewById(R.id.bt_salvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(GerenciamentoCupomActivity.this, getCurrentFocus());
                presenter.salvarCupom();
            }
        });

        ilimidado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                quantidade.setText(null);
                quantidade.setError(null);
                quantidade.setEnabled(!isChecked);
            }
        });

        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra("tela").equals("cadastro")) {
                setLayoutCadVisibility(true);
            } else {
                setLayoutListVisibility(true);
                presenter.retriveCupons();
                adapter = new CuponsAdapter(this, presenter.getList());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (cad.getVisibility() == View.VISIBLE) {
            if (presenter.fechaTela()) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title_sair_casdastro))
                        .setMessage(getString(R.string.dialog_mensagem_cadastro))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                GerenciamentoCupomActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                GerenciamentoCupomActivity.super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Map<String, Object> getInformacoes() {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", codigo.getText().toString().trim());
        map.put("desconto", valor.getText().toString().trim());
        map.put("quantidade", ilimidado.isChecked() ? -1 : quantidade.getText().toString().trim());
        map.put("limite", 0);
        return map;
    }

    @Override
    public void setErro(String campo, String menssagem) {
        switch (campo) {
            case "nome":
                codigo.requestFocus();
                codigo.setError(menssagem);
                break;
            case "desconto":
                valor.requestFocus();
                valor.setError(menssagem);
                break;
            case "quantidade":
                quantidade.requestFocus();
                quantidade.setError(menssagem);
                break;
            default:
                SpAndToast.showMessage(this, menssagem);
                break;
        }
    }

    @Override
    public void setLayoutListVisibility(boolean visibility) {
        findViewById(R.id.cl_list).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLayoutCadVisibility(boolean visibility) {
        cad.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setVisibilityList(boolean visibility) {
        recyclerView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        findViewById(R.id.tx_sem_cupom).setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(final Cupom cupom) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_delete))
                .setMessage(getString(R.string.dialog_mensagem_deletar) + ": " + cupom.getId())
                .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deletaCupom(cupom);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void limparCampos() {
        valor.setText(null);
        codigo.setText(null);
        quantidade.setText(null);
        ilimidado.setChecked(false);
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        }
    }
}