package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;

public interface Caixa {
    interface ModelCaixa {
        void consultaMesa(String mesa);

        void buscaNomeContas();

        void verificaPedidosAbertos(String uidItem);

        void fechaContas(String uID);

        void buscaPedidos(String mesa, String uID);

        void consultaCodigoDesconto(String codigo);

        void salvaLimiteDesconto(Float limite, String codigo);

        String obterUidCaixa();

        void salvaVenda(HashMap<String, Object> map);

        void mudaStatusPedidos(String pedidos);

        void excluiPedidoMesa(String uid);

        void excluiItens(String uidIten, String uid);

        void excluiUid(String uid);

        void buscaValorFechamento();

        void salvaValorFechamento(Float valor);

        void buscaDadosCaixa();

        void verificaCaixaAberto();

        void abreCaixa(Map<String, Object> abertura);

        void fecharCaixa();
    }

    interface EscolherMesaActivity {
        void mesaErros(int i);

        void setButtonEnabled(boolean enabled);

        void setProgressVisibility(boolean visibility);

        void alertFecharMesa(String titulo, String texto);

        void startActivity();

        void startActivityFecharCaixa();
    }

    interface EscolherMesaPresenter {
        Context getContext();

        void buscaMesa(String mesa);

        void responseConsultaMesa(Task<QuerySnapshot> task, String mesa);

        void alertFecharMesa();

        void fecharCaixa();

        void responseFecharCaixa(Task<Void> task);
    }

    interface EscolherContasActivity {
        void setButtonEnabled(boolean enabled);

        void setProgressVisibility(boolean visibility);

        void notificaLista();

        void criaLista();

        void startActivity();
    }

    interface EscolherContasPresenter {
        Context getContext();

        void buscaNomesContas();

        void responseNomesContas(Task<QuerySnapshot> task);

        void responseVerificaPedidos(Task<DocumentSnapshot> task);

        void responseListPedidos(Task<QuerySnapshot> task);

        void responseListfechaConta(Task<Void> task);

        ArrayList<CaixaObj> getListPedidos();

        List<CaixaObj> getListClientes();

        List<String> getListClientesUIDS();

        void buscaPedidos();
    }

    interface PedidosActivity {
        void criaLista();

        void alertCupom(String titulo, String texto);

        void setButtonEnabled(boolean enabled);

        void setProgressVisibility(boolean visibility);

        void notificaLista();

        TextView totalItens();

        TextView valorTotal();

        CheckBox checkBoxDezPorcento();

        void alertAviso(String titulo, String texto);

        void alertEscolherFormaPagamento(String titulo, ArrayList<String> itens);

        void alertValorPago(String titulo, String texto);

        void alertCpf(String titulo, String texto);

        void startActivity(String valorPagoCliente, float valorTotal);
    }

    interface PedidosPresenter {
        Context getContext();

        void setListPedidos(ArrayList<CaixaObj> listPedidos);

        ArrayList<CaixaObj> getListPedidos();

        void totalItens();

        void valorTotalComDez();

        void valorTotalSemDez();

        void alertCupom();

        void consultaCodigoDesconto(String codigo);

        void responseCodigoDesconto(Task<DocumentSnapshot> task, String codigo);

        void responseLimiteDesconto(Task<Void> task);

        void avisoPagamento();

        void EscolherFormaPagamento();

        void valorPago(String opcaoPagamento);

        void cpf(String valorPago);

        void uidCaixa(String cpf);

        void responseVenda(Task<DocumentReference> task);

        void responseMudarStatus(Task<Void> task);

        void responseExcluiPedidoMesa(Task<QuerySnapshot> task, String uid);

        void responseExcluiItens(Task<Void> task, String uid);

        void responseExcluiUid(Task<Void> task);

        void responseBuscaValorFechamento(Task<DocumentSnapshot> task);

        void responseSalvaValorFechamento(Task<Void> task);
    }

    interface PagamentoConcluidoActivity {
        void setProgressVisibility(boolean visibility);

        TextView valor();

        TextView troco();
    }

    interface PagamentoConcluidoPresenter {
        Context getContext();

        void subtraiValores(String valorPagoCliente, Float valorTotal);
    }

    interface AberturaActivity extends InterfaceBase.ButtonEnabled {
        void mesaErros(int i);

        void startActivity();

        void startActivityMenu();
    }

    interface AberturaPresenter {
        Context getContext();

        void verificaCaixaAberto();

        void responseVerificaCaixaAberto(Task<DocumentSnapshot> task);

        void aberturaCaixa(String valor);

        void responseBuscaUidCaixa(Task<DocumentSnapshot> task);

        void responseAbreCaixa(Task<Void> task);
    }
}