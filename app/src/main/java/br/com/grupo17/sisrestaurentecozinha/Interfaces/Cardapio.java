package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;

public interface Cardapio {
    interface ModelCardapio {
        void buscarItens(String categoria);

        void criaRefDeletaFotoComUri(Uri pathFoto);

        void criaRefDeletaFotoComUrl(String url);

        void deletaItem(String categoria, String idItem);

        void salvaCardapioObj(CardapioObj cardapioObj);

        void salvaFotoItem(Uri uri, String categoria);

        void updateCampoPathFoto(String categoria, String idItem, String path);

        void updateItem(CardapioObj cardapioObj);
    }

    interface CardapioView extends InterfaceBase.ProgressVisibility {
        void setInfoSemRegistro();

        void setButtonBack();

        void setSubTitle(String subTitle);

        void updateListaRecycler();

        void onClickCardapioCategoria(String categoria);

        void onClickSelecionouItem(CardapioObj cardapioObj);
    }

    interface CardapioPresenter {
        void buscarItens(String categoria);

        List<CardapioObj> getList();

        Context getContext();

        void responseBusca(QuerySnapshot queryDocumentSnapshots);

        void setErro(Exception e);
    }

    interface CadastroView extends InterfaceBase.ButtonEnabled {
        void setError(int campo, String mensagem);

        void setImagemImageView(Uri pathImg);

        void setTextNaAtualizacao();

        void abrirGaleria();

        HashMap<String, String> getInformacao();

        void limpaCampos();

        void fechaComResult();
    }

    interface CadastroPresenter {
        Context getContext();

        CardapioObj getCardapio();

        void onResult(int requestCode, int resultCode, Intent data);

        void salvaCardapioObj();

        void responseSalvaItem(Task<DocumentReference> task);

        void responseSaltaFoto(Task<Uri> task);

        void responseUpdateCampoPathFoto(Task<Void> task);

        void responseUpdateItem(Task<Void> task);

        void responseDeletaFoto(Task<Void> task);

        void responseDeletaItem(Task<Void> task);

        void verificaPermission();

        void abreGaleria();

        boolean fecharTela();
    }

    interface DetalheView {
        Context getContext();

        void finalizaDeleteItem();

        void setTextInfo();

        void setProgressVisibility(int qualProgress, boolean visibility);
    }

    interface DetalhePresenter {
        void deletaItem();

        Context getContext();

        CardapioObj getItem();

        void responseDelete(int operacao, Task<Void> task);

        void onResult(int requestCode, int resultCode, Intent data);
    }
}