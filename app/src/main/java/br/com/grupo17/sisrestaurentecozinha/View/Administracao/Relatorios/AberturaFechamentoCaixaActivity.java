package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Relatorios;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaRelatorio;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Relatorios.CaixaRelatorioPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.RelatorioCaixaAdapter;

public class AberturaFechamentoCaixaActivity extends AppCompatActivity implements Administracao.CaixaRelatorioView {
    private TextView nome, cpf, status, data, abertura, fechamento;
    private Administracao.CaixaRelatorioPresenter presenter;
    private RelatorioCaixaAdapter adapter;
    private ConstraintLayout detalhes;
    private Button bt;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abertura_fechamento_caixa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        id = "";
        cpf = findViewById(R.id.tx_cpf);
        data = findViewById(R.id.tx_data);
        nome = findViewById(R.id.tx_nome);
        bt = findViewById(R.id.bt_acaoCaixa);
        status = findViewById(R.id.tx_status);
        detalhes = findViewById(R.id.cl_detalhes);
        abertura = findViewById(R.id.tx_abertura);
        fechamento = findViewById(R.id.tx_fechamento);
        RecyclerView recyclerView = findViewById(R.id.rv_list);

        recyclerView.setHasFixedSize(true); //informando que os itens da lista não vão sofre alteração de tamanho.
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm); //seta o tipo de layout da lista

        presenter = new CaixaRelatorioPresenter(this);
        presenter.retriveDadosCaixa(); //busca dados caixa

        adapter = new RelatorioCaixaAdapter(this, presenter.getList());
        recyclerView.setAdapter(adapter);

        //adiciona a mascara do tipo de CPF no campo do CPF
        cpf.addTextChangedListener(
                new MaskTextWatcher(cpf, new SimpleMaskFormatter("NNN.NNN.NNN-NN"))
        );
    }

    @Override
    public void onBackPressed() {
        if (detalhes.getVisibility() == View.VISIBLE) {
            setVisibillityList(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(final CaixaRelatorio caixaRelatorio) {
        id = caixaRelatorio.getId();
        nome.setText(caixaRelatorio.getNome());
        status.setText(caixaRelatorio.getStatus());
        cpf.setText(caixaRelatorio.getCpf());
        data.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(caixaRelatorio.getData()));

        BigDecimal aNumber = new BigDecimal(caixaRelatorio.getAbertura());
        aNumber = aNumber.setScale(2, BigDecimal.ROUND_HALF_UP);
        abertura.setText(String.valueOf(aNumber));

        aNumber = new BigDecimal(caixaRelatorio.getFechamento());
        aNumber = aNumber.setScale(2, BigDecimal.ROUND_HALF_UP);
        fechamento.setText(String.valueOf(aNumber));

        if (caixaRelatorio.getStatus().equals("aguardando conferencia")) {
            bt.setText("Conferido");
            bt.setVisibility(View.VISIBLE);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.updateStatus(caixaRelatorio);
                }
            });
        } else if (caixaRelatorio.getStatus().equals("fechado")) {
            bt.setText("Liberar Caixa");
            bt.setVisibility(View.VISIBLE);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.deletarItem(caixaRelatorio);
                }
            });
        } else {
            bt.setVisibility(View.GONE);
        }

        setVisibillityList(false);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public String getIdSetado() {
        return id;
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        bt.setEnabled(enabled);
    }

    @Override
    public void setVisibillityList(boolean visibility) {
        if (visibility){
            id = "";
        }
        findViewById(R.id.cl_list).setVisibility(visibility ? View.VISIBLE : View.GONE);
        detalhes.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void semItem(boolean visibility) {
        findViewById(R.id.tx_sem_caixa).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}