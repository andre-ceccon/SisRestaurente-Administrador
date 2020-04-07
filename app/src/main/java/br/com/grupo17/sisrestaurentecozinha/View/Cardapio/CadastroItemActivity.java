package br.com.grupo17.sisrestaurentecozinha.View.Cardapio;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio.CadastraItemPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;
import br.com.grupo17.sisrestaurentecozinha.Util.Teclado;
import faranjit.currency.edittext.CurrencyEditText;

public class CadastroItemActivity extends AppCompatActivity implements Cardapio.CadastroView {
    private Cardapio.CadastroPresenter presenter;
    private EditText nome, descricao;
    private CurrencyEditText preco;
    private ImageView imageView;
    private Spinner spinner;
    private Button salvar;
    private Switch status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back); //adicionando a flecha de voltar a tela
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); //Onclick da flecha

        preco = findViewById(R.id.edt_preco);
        nome = findViewById(R.id.edt_nome);
        descricao = findViewById(R.id.edt_descricao);
        status = findViewById(R.id.sw_status);
        imageView = findViewById(R.id.iv_foto);
        spinner = findViewById(R.id.spinner_item);
        salvar = findViewById(R.id.bt_salvar);

        List<String> plantsList = new ArrayList<>();
        plantsList.add(getString(R.string.campo_spinner));
        plantsList.addAll(Arrays.asList(getResources().getStringArray(R.array.menu_array))); //Pega uma array de String do arquivo arrays.xml e adiciona na lista

        /*
         * Cria um adapter de String para o Spinner em que
         * o click  do elemento da possição zero fica desabilidado
         * e com a cor cinza;
         */
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, plantsList) {
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
        spinner.setAdapter(spinnerArrayAdapter);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teclado.hideKeyboard(CadastroItemActivity.this, salvar);
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                presenter.salvaCardapioObj();
            }
        });

        findViewById(R.id.bt_imagem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Verifica se o aplicativo tem permissão para acessar
                 * o armazenamento interno do dispositivo para visualizar
                 * as imagens e então selecionar alguma;
                 */
                presenter.verificaPermission();
            }
        });

        if (getIntent().getParcelableExtra(getString(R.string.key_item)) != null) {
            //Pega o item que foi passado na abertura dessa tela, atraves da intent
            CardapioObj obj = getIntent().getParcelableExtra(getString(R.string.key_item));
            presenter = new CadastraItemPresenter(this, obj);
            setTextNaAtualizacao();
        } else {
            presenter = new CadastraItemPresenter(this);
        }
    }

    /*
     * Verifica se tem alguma alteração das informações digitadas
     * antes de fechar a tela. Caso tenha, é perguntado para o
     * usuario se deseja realmente sair sem salvar;
     */
    @Override
    public void onBackPressed() {
        if (presenter.fecharTela()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_title_sair_casdastro))
                    .setMessage(getString(R.string.dialog_mensagem_cadastro))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_button_dialog_sim), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            CadastroItemActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_button_dialog_nao), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    /*
     * Quando uma activity é aberta em modo que retorna um valor,
     * startActivityForResult,
     * no retorno volta para esse metodo.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onResult(requestCode, resultCode, data); //resultado processado no preesenter
    }

    /*
     * Método que abre a galeria de fotos, com a possibilidade de selecionar alguma foto
     */
    @Override
    public void abrirGaleria() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 12);
    }

    /*
     * Método para fechar tela,
     * O item que estava sendo editado é adicionado para ser passado para a outra tela,
     * junto com o resultado da tela que é Ok, quando é setado nesse método;
     */
    @Override
    public void fechaComResult() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.key_item), presenter.getCardapio());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /*
     * Método usado para pegar as informações do formulario;
     */
    @Override
    public HashMap<String, String> getInformacao() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(getString(R.string.key_campo_nome_prato), nome.getText().toString().trim());
            map.put(getString(R.string.key_campo_descricao), descricao.getText().toString().trim());
            map.put(getString(R.string.key_campo_preco), preco.getCurrencyText());
            map.put(getString(R.string.key_campo_statusitem), status.isChecked() ? getString(R.string.key_item_disponivel_cardapio) : getString(R.string.key_item_nao_disponivel_cardapio));
            map.put(getString(R.string.key_campo_categoria), spinner.getSelectedItem().toString());
            return map;
        } catch (ParseException e) {
            SpAndToast.showMessage(this, getString(R.string.erro_ao_parse_preco));
            return null;
        }
    }

    /*
     * Apaga as informações que o usuario informou
     */
    @Override
    public void limpaCampos() {
        nome.setText(null);
        descricao.setText(null);
        preco.setText(null);
        imageView.setImageResource(R.drawable.logo);
        spinner.setSelection(0);
        status.setChecked(false);
    }

    /*
     * Método usado habilitar ou desablitar o click do botão
     */
    @Override
    public void setButtonEnabled(boolean enabled) {
        salvar.setEnabled(enabled);
    }

    /*
     * Método usado para apresentar a mensagem de erro;
     */
    @Override
    public void setError(int campo, String mensagem) {
        switch (campo) {
            case R.string.campo_nome:
                nome.setError(mensagem);
                nome.requestFocus();
                Teclado.showKeyboard(this);
                break;
            case R.string.campo_descricao:
                descricao.setError(mensagem);
                descricao.requestFocus();
                Teclado.showKeyboard(this);
                break;
            case R.string.campo_preco:
                preco.setError(mensagem);
                preco.requestFocus();
                Teclado.showKeyboard(this);
                break;
            case R.string.aviso_imageView:
                SpAndToast.showMessage(this, mensagem);
                break;
            case R.string.campo_spinner:
                SpAndToast.showMessage(this, mensagem);
                break;
            default:
                SpAndToast.showMessage(this, mensagem);
                break;
        }
    }

    /*
     * Método utilizado apos a seleção de uma imagem, para o setamento da mesma no componente ImageView;
     */
    @Override
    public void setImagemImageView(Uri pathImg) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathImg.getPath());
        Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1080, 1000, true);
        imageView.setImageBitmap(bitmapReduzido);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /*
     * Método usado para setar o texto de um item
     * quando for selecioado a edição do mesmo;
     */
    @Override
    public void setTextNaAtualizacao() {
        CardapioObj obj = presenter.getCardapio();
        Adapter adapter = spinner.getAdapter();

        nome.setText(obj.getNome());
        descricao.setText(obj.getDescricao());
        status.setChecked(obj.getStatus().equals(getString(R.string.key_item_disponivel_cardapio)));

        //formata o preco para no maximo duas casas decimais;
        BigDecimal aNumber = new BigDecimal(obj.getPreco());
        preco.setText(String.valueOf(aNumber.setScale(2, BigDecimal.ROUND_HALF_UP)));

        if (!TextUtils.isEmpty(obj.getPathFoto())) {
            setProgressVisibility(true);
            //Baixa a imagem do Firebase Storage
            Picasso.get()
                    .load(obj.getPathFoto())
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            setProgressVisibility(false);
                        }

                        @Override
                        public void onError(Exception e) {
                            setProgressVisibility(false);
                            android.widget.Toast.makeText(CadastroItemActivity.this, R.string.erro_baixar_imagem, android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        //Pegando a posição do texto na lista e setando o spinner para a posição correspontente
        for (int i = 0; i < adapter.getCount(); i++) {
            if (obj.getCategoria().equals(adapter.getItem(i))) {
                spinner.setSelection(i);
            }
        }
    }
}