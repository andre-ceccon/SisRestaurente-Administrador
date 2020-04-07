package br.com.grupo17.sisrestaurentecozinha.View.Cardapio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio.DetalheItemPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class DetalheItemActivity extends AppCompatActivity implements Cardapio.DetalheView {
    private TextView nome, categoria, preco, descricao, numeroAvaliacao;
    private Cardapio.DetalhePresenter detalhePresenter;
    private ImageButton editar, deletar;
    private ProgressBar progressBar;
    private RatingBar avaliacao;
    private ImageView imageView;
    private Switch status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        nome = findViewById(R.id.tx_nome);
        categoria = findViewById(R.id.tx_categoria);
        preco = findViewById(R.id.tx_preco);
        descricao = findViewById(R.id.tx_descricao);
        numeroAvaliacao = findViewById(R.id.tx_avaliacao);
        avaliacao = findViewById(R.id.avaliacao);
        imageView = findViewById(R.id.iv_foto);
        status = findViewById(R.id.sw_status);
        status.setEnabled(false);

        if (getIntent().getParcelableExtra(getString(R.string.key_item)) != null) {
            //Pega o item que foi passado na abertura dessa tela, atraves da intent
            CardapioObj obj = getIntent().getParcelableExtra(getString(R.string.key_item));
            detalhePresenter = new DetalheItemPresenter(this, obj);
        }

        editar = findViewById(R.id.bt_editar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre a tela de cadastro para a edição do item, passando o item atraves da intent;
                Intent intent = new Intent(DetalheItemActivity.this, CadastroItemActivity.class);
                intent.putExtra(getString(R.string.key_item), detalhePresenter.getItem());
                startActivityForResult(intent, 1);
            }
        });

        deletar = findViewById(R.id.bt_deletar);
        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pergunta se o usuario quer realmente deletar o item
                new AlertDialog.Builder(DetalheItemActivity.this)
                        .setTitle(getString(R.string.dialog_title_delete))
                        .setMessage(getString(R.string.dialog_mensagem_deletar) + ": " + detalhePresenter.getItem().getNome() + "?")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                detalhePresenter.deletaItem();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTextInfo();
    }

    /*
     * Quando uma activity é aberta em modo que retorna um valor,
     * startActivityForResult,
     * no retorno volta para esse metodo.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detalhePresenter.onResult(requestCode, resultCode, data);
    }

    /*
     * Informa que foi deletado com sucesso e fecha a tela;
     */
    @Override
    public void finalizaDeleteItem() {
        setProgressVisibility(R.string.key_delete, false);
        SpAndToast.showMessage(this, getString(R.string.aviso_deletar_sucesso));
        finish();
    }

    /*
     * Método para retorna o contexto da tela
     */
    @Override
    public Context getContext() {
        return this;
    }

    /*
     * Método usado para setar o texto de um item
     */
    @Override
    public void setTextInfo() {
        CardapioObj obj = detalhePresenter.getItem();
        if (obj.getNome() != null) {
            if (obj.getNome().contains(getString(R.string.erro_pedido))) {
                deletar.setVisibility(View.GONE);
                editar.setVisibility(View.GONE);
            }
            nome.setText(obj.getNome());
        } else {
            nome.setText("");
        }

        categoria.setText(obj.getCategoria() != null ? obj.getCategoria() : "");
        descricao.setText(obj.getDescricao() != null ? obj.getDescricao() : "");
        numeroAvaliacao.setText(obj.getQuantidadeAV() != null ? String.valueOf(obj.getQuantidadeAV()) : "");
        avaliacao.setRating(obj.getAvaliacao() != null ? obj.getAvaliacao() : 0);
        status.setChecked(obj.getStatus() != null && obj.getStatus().equals(getContext().getString(R.string.key_item_disponivel_cardapio)));
        preco.setText(
                NumberFormat.getInstance(new Locale("en", "US"))
                        .format(obj.getPreco() != null ? obj.getPreco() : 0)
        );

        if (!TextUtils.isEmpty(obj.getPathFoto())) {
            setProgressVisibility(R.string.key_campo_foto, true);
            //Baixa a imagem do Firebase Storage
            Picasso.get()
                    .load(obj.getPathFoto())
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            setProgressVisibility(R.string.key_campo_foto, false);
                        }

                        @Override
                        public void onError(Exception e) {
                            setProgressVisibility(R.string.key_campo_foto, false);
                            android.widget.Toast.makeText(DetalheItemActivity.this, R.string.erro_baixar_imagem, android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(int qualProgress, boolean visibility) {
        switch (qualProgress) {
            case R.string.key_campo_foto:
                progressBar = findViewById(R.id.progress);
                break;
            case R.string.key_delete:
                progressBar = findViewById(R.id.progressDelete);
                break;
        }

        progressBar.setVisibility(visibility ? ProgressBar.VISIBLE : ProgressBar.GONE);
    }
}