package br.com.grupo17.sisrestaurentecozinha.View.Cardapio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.Presenter.Cardapio.CardapioPresenter;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.ArrayStringAdapter;
import br.com.grupo17.sisrestaurentecozinha.View.Adapter.CardapioSelecionouAdapter;

public class GerenciadorCardapioActivity extends AppCompatActivity implements Cardapio.CardapioView {
    private Cardapio.CardapioPresenter cardapioPresenter;
    private CardapioSelecionouAdapter selecionouAdapter;
    private ArrayStringAdapter categoriasAdapter;
    private RecyclerView recyclerView;
    private TextView semRegistro;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciador_cardapio);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonBack();

        recyclerView = findViewById(R.id.rvCard);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        findViewById(R.id.bt_novo_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abre a tela de cadastro
                startActivity(new Intent(GerenciadorCardapioActivity.this, CadastroItemActivity.class));
                toolbar.setSubtitle(null);
            }
        });

        cardapioPresenter = new CardapioPresenter(this);
        categoriasAdapter = new ArrayStringAdapter(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_array))), this);
        selecionouAdapter = new CardapioSelecionouAdapter(cardapioPresenter.getList(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapterCategoria();
        setSubTitle(null);
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.INVISIBLE || recyclerView.getAdapter() == selecionouAdapter) {
            if (semRegistro != null && semRegistro.getVisibility() == View.VISIBLE) {
                semRegistro.setVisibility(View.GONE);
            }
            setSubTitle(null);
            recyclerView.setVisibility(View.VISIBLE);
            setAdapterCategoria();
        } else {
            super.onBackPressed();
        }
    }

    /*
     * Selecionou Categoria cardapio
     */
    @Override
    public void onClickCardapioCategoria(String categoria) {
        cardapioPresenter.buscarItens(categoria);
    }

    /*
     * Selecionou um item cadastrado
     */
    @Override
    public void onClickSelecionouItem(CardapioObj cardapioObj) {
        Intent intent = new Intent(this, DetalheItemActivity.class);
        intent.putExtra(getString(R.string.key_item), cardapioObj);
        startActivity(intent);
    }

    /*
     * Seta o adaptar da categoria na lista
     */
    public void setAdapterCategoria() {
        recyclerView.setAdapter(categoriasAdapter);
    }

    /*
     * Adicionando a flecha de voltar a tela e o Onclick da flecha
     */
    @Override
    public void setButtonBack() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
     * Habilita um TextView informando que não há item cadastrado
     */
    @Override
    public void setInfoSemRegistro() {
        recyclerView.setVisibility(View.INVISIBLE);
        semRegistro = findViewById(R.id.tx_sem_itens);
        semRegistro.setVisibility(View.VISIBLE);
        semRegistro.setText(getString(R.string.aviso_sem_item_cadastrado));
    }

    /*
     * Método usado para esconder e mostrar o componente ProgressBar na tela;
     */
    @Override
    public void setProgressVisibility(boolean visibility) {
        findViewById(R.id.progress).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /*
     * Seta o subTitle na toolbar
     */
    @Override
    public void setSubTitle(String subTitle) {
        toolbar.setSubtitle(subTitle);
    }

    /*
     * Seta o adaptar de uma categoria selecionada na lista
     * com os itens já baixados
     */
    @Override
    public void updateListaRecycler() {
        recyclerView.setAdapter(selecionouAdapter);
    }
}