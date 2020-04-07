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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.HorarioFuncionamenoPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.ArrayStringAdapter;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.FuncionamentoAdapter;

public class HorarioFuncionamentoViewActivity extends AppCompatActivity implements Administracao.HorarioFuncionamentoView {
    private Administracao.HorarioFuncionamentoPresenter presenter;
    private FuncionamentoAdapter funcionamentoAdapter;
    private ArrayStringAdapter adapterArrayString;
    private RecyclerView recyclerView;
    private TextView semRegistro;
    private Toolbar toolbar;
    private Button novo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horario_funcionamento);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        novo = findViewById(R.id.bt_novo_horario);
        recyclerView = findViewById(R.id.rvhorarios);
        semRegistro = findViewById(R.id.tv_sem_registro_horario);

        findViewById(R.id.bt_novo_horario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HorarioFuncionamentoViewActivity.this, CadastrosHorariosActivity.class);
                intent.putExtra(getString(R.string.key_intent_tela), getString(R.string.key_tela_normal));
                intent.putExtra(getString(R.string.key_intent_dia), toolbar.getSubtitle().toString());
                startActivityForResult(intent, 1);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        presenter = new HorarioFuncionamenoPresenter(this);

        adapterArrayString = new ArrayStringAdapter(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dias_semanas))), this);
        funcionamentoAdapter = new FuncionamentoAdapter(this, presenter.getHorariosCadastrados());
        updateListComDiasSemanas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dias_especiais, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.especias) {
            startActivity(new Intent(this, HorarioEspecialActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.INVISIBLE || recyclerView.getAdapter() == funcionamentoAdapter) {
            if (semRegistro != null && semRegistro.getVisibility() == View.VISIBLE) {
                semRegistro.setVisibility(View.GONE);
            }
            setSubTitle(null);
            recyclerView.setVisibility(View.VISIBLE);
            updateListComDiasSemanas();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                presenter.retriveHorarios(data.getStringExtra(getString(R.string.key_item)));
            }
        }
    }

    @Override
    public void onClickDiaSemana(String dia) {
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
                        presenter.deletarHorario(toolbar.getSubtitle().toString(), horario);
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
    public void setVissibilityButtom(boolean vissibility) {
        novo.setVisibility(vissibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecyclerVisibility(boolean visibility) {
        recyclerView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setInfoSemRegistro(boolean estado) {
        recyclerView.setVisibility(estado ? View.INVISIBLE : View.VISIBLE);
        semRegistro.setVisibility(estado ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSubTitle(String menssagem) {
        toolbar.setSubtitle(menssagem);
    }

    @Override
    public void updateListComHorarios() {
        setVissibilityButtom(true);
        recyclerView.setAdapter(funcionamentoAdapter);
    }

    @Override
    public void updateListComDiasSemanas() {
        setVissibilityButtom(false);
        recyclerView.setAdapter(adapterArrayString);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}