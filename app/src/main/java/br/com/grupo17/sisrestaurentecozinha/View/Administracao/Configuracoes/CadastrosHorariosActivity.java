package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.CadastrosHorariosPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class CadastrosHorariosActivity extends AppCompatActivity implements Administracao.CadastroHorariosView {
    private Administracao.CadastroHorariosPresenter presenter;
    private Spinner abertura, fechamento;
    private String dia, tela;
    private CheckBox fechado;
    private EditText et_dia;
    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastros_horarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getExtras() != null) {
            dia = getIntent().getStringExtra(getString(R.string.key_intent_dia));
            tela = getIntent().getStringExtra(getString(R.string.key_intent_tela));
        }

        et_dia = findViewById(R.id.tx_dia);
        fechado = findViewById(R.id.cb_box);
        abertura = findViewById(R.id.sp_abertura);
        salvar = findViewById(R.id.bt_cad_horario);
        salvar = findViewById(R.id.bt_cad_horario);
        fechamento = findViewById(R.id.sp_fechamento);
        TextInputLayout textLayout = findViewById(R.id.textInputLayout8);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia = TextUtils.isEmpty(dia) ? et_dia.getText().toString() : dia;
                Teclado.hideKeyboard(CadastrosHorariosActivity.this, salvar);
                presenter.salvaHorarios(
                        dia, abertura.getSelectedItem().toString(),
                        fechamento.getSelectedItem().toString(),
                        tela,
                        fechado.isChecked()
                );
            }
        });

        if (tela.equals(getString(R.string.key_tela_especial))) {
            textLayout.setVisibility(View.VISIBLE);
            fechado.setVisibility(View.VISIBLE);

            findViewById(R.id.textView19).setVisibility(View.VISIBLE);

            fechado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        abertura.setEnabled(false);
                        fechamento.setEnabled(false);
                    } else {
                        abertura.setEnabled(true);
                        fechamento.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new CadastrosHorariosPresenter(this);
        presenter.preparaListCadastro();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, presenter.getList()) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        abertura.setAdapter(spinnerAdapter);
        fechamento.setAdapter(spinnerAdapter);

        et_dia.addTextChangedListener(
                new MaskTextWatcher(et_dia,
                        new SimpleMaskFormatter("NN-N-NNNN")
                )
        );
    }

    @Override
    public void setErro(String mensagem) {
        et_dia.requestFocus();
        et_dia.setError(mensagem);
        Teclado.showKeyboard(this);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        salvar.setEnabled(enabled);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void finishAcao() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.key_item), dia);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}