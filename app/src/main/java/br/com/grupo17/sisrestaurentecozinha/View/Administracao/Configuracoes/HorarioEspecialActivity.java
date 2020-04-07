package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.HorarioEspecialPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.ArrayStringAdapter;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.FuncionamentoAdapter;

public class HorarioEspecialActivity extends AppCompatActivity implements Administracao.HorarioEspecialView {
    private Administracao.HorarioEspecialPresenter presenter;
    private FuncionamentoAdapter funcionamentoAdapter;
    private ArrayStringAdapter adapterArrayString;
    private RecyclerView recyclerView;
    private TextView semRegistro;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horario_especial);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        semRegistro = findViewById(R.id.txt_horario);
        recyclerView = findViewById(R.id.rv_especial);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        presenter = new HorarioEspecialPresenter(this);

        findViewById(R.id.bt_adcionar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HorarioEspecialActivity.this, CadastrosHorariosActivity.class);
                intent.putExtra(getString(R.string.key_intent_tela), getString(R.string.key_tela_especial));
                intent.putExtra(getString(R.string.key_intent_dia), "");
                startActivityForResult(intent, 2);
            }
        });

        adapterArrayString = new ArrayStringAdapter(presenter.getListDias(), this);
        funcionamentoAdapter = new FuncionamentoAdapter(this, presenter.getListHorarios());
        setAdapterDias();

        presenter.retriveDiasEspeciais();
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() == funcionamentoAdapter) {
            setAdapterDias();
            setSubTitle(null);
            setInfoSemRegistro(false);
            presenter.retriveDiasEspeciais();
        } else {
            setInfoSemRegistro(false);
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String dia = data.getStringExtra(getString(R.string.key_item));
                setSubTitle(dia);
                presenter.retriveHorarios(dia);
            }
        }
    }

    @Override
    public void onClickDia(String dia) {
        setSubTitle(dia);
        presenter.retriveHorarios(dia);
    }

    @Override
    public void onClickDelete(final String horario) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_delete))
                .setMessage(getString(R.string.dialog_mensagem_deletar) + ": " + horario)
                .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteHorario(toolbar.getSubtitle().toString(), horario);
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
    public void setAdapterDias() {
        adapterArrayString.notifyDataSetChanged();
        recyclerView.setAdapter(adapterArrayString);
    }

    @Override
    public void setAdapterHorario() {
        recyclerView.setAdapter(funcionamentoAdapter);
    }

    @Override
    public void setSubTitle(String subTitle) {
        toolbar.setSubtitle(subTitle);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecyclerVisibility(boolean visibility) {
        recyclerView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setInfoSemRegistro(boolean estado) {
        semRegistro.setVisibility(estado ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(estado ? View.INVISIBLE : View.VISIBLE);
    }
}