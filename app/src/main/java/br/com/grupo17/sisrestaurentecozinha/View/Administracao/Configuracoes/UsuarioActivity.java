package br.com.grupo17.sisrestaurentecozinha.View.Administracao.Configuracoes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.User;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes.UsuarioPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;

public class UsuarioActivity extends AppCompatActivity implements Administracao.UsuarioView {
    private TextView nome, email, celular, nascimento, nivel, mesa, tx_cpf;
    private Button consultar, resetar, alterar, salvar;
    private Administracao.UsuarioPresenter presenter;
    private ConstraintLayout layNivelUser;
    private Spinner spNiveis;
    private EditText et_cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_cpf = findViewById(R.id.et_cpf);
        nome = findViewById(R.id.txt_nome);
        mesa = findViewById(R.id.txt_mesa);
        tx_cpf = findViewById(R.id.txt_cpf);
        nivel = findViewById(R.id.txt_nivel);
        email = findViewById(R.id.txt_email);
        salvar = findViewById(R.id.bt_salvar);
        alterar = findViewById(R.id.bt_alterar);
        celular = findViewById(R.id.txt_celular);
        spNiveis = findViewById(R.id.sp_nivel_user);
        consultar = findViewById(R.id.bt_consultar);
        resetar = findViewById(R.id.bt_resetar_mesa);
        nascimento = findViewById(R.id.txt_nascimento);
        layNivelUser = findViewById(R.id.lay_altera_nivel);

        presenter = new UsuarioPresenter(this);

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(UsuarioActivity.this, getCurrentFocus());
                presenter.retriveUsuario(et_cpf.getText().toString().replace(".", "").replace("-", "").trim());
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateNivel(spNiveis.getSelectedItem().toString());
            }
        });

        resetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UsuarioActivity.this)
                        .setTitle(getString(R.string.dialog_title_resetar))
                        .setMessage(getString(R.string.dialog_mensagem_resetar))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                presenter.resetaMesa();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        findViewById(R.id.bt_alterar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preparaAlteraNivel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Mascara cpf
        SimpleMaskFormatter formatterCPF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");

        et_cpf.addTextChangedListener(
                new MaskTextWatcher(et_cpf, formatterCPF)
        );

        tx_cpf.addTextChangedListener(
                new MaskTextWatcher(tx_cpf, formatterCPF)
        );

        celular.addTextChangedListener(
                new MaskTextWatcher(celular, new SimpleMaskFormatter("(NN) N NNNN-NNNN"))
        );
    }

    @Override
    public void setText(User user) {
        String s = user.getNome() + " " + user.getSobrenome();
        nome.setText(s);
        mesa.setText(user.getMesa());
        tx_cpf.setText(user.getCpf());
        email.setText(user.getEmail());
        nivel.setText(user.getNivelUser());
        celular.setText(user.getCelular());
        nascimento.setText(user.getDataDeNascimento());
    }

    @Override
    public void setErro(String mgs) {
        et_cpf.setError(mgs);
        et_cpf.requestFocus();
        Teclado.showKeyboard(this);
    }

    @Override
    public void limparCampo() {
        et_cpf.setText("");
        et_cpf.clearFocus();
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLayVisibility(boolean visibility) {
        layNivelUser.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setButtonEnabled(int campo, boolean enabled) {
        switch (campo) {
            case R.string.key_usuario_consulta:
                consultar.setEnabled(enabled);
                break;
            case R.string.key_usuario_consultaSucesso:
                alterar.setEnabled(enabled);
                resetar.setEnabled(enabled);
                break;
            case R.string.key_campo_nivelUser:
                salvar.setEnabled(enabled);
                break;
            case R.string.key_usuario_resetar:
                resetar.setEnabled(enabled);
                break;
        }
    }

    private void preparaAlteraNivel() {
        String[] array = getResources().getStringArray(R.array.niveis_user);
        spNiveis.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array));
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(nivel.getText().toString())) {
                spNiveis.setSelection(i);
            }
        }
        setLayVisibility(true);
    }
}