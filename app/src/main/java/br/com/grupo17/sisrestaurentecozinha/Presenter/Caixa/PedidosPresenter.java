package br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class PedidosPresenter implements Caixa.PedidosPresenter {
    private boolean ultimoPedido, itenspedido;
    private Boolean descontoUnico = false;
    private Boolean dezPorcento = false;
    private HashMap<String, Object> map;
    private ArrayList<CaixaObj> pedidos;
    private Caixa.PedidosActivity view;
    private float valorDesconto = 0;
    private String valorPagoCliente;
    private Caixa.ModelCaixa model;
    private float valorTotal;

    public PedidosPresenter(Caixa.PedidosActivity view) {
        this.view = view;
        this.map = new HashMap<>();
        this.pedidos = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void setListPedidos(ArrayList<CaixaObj> listPedidos) {
        pedidos.addAll(listPedidos);
    }

    @Override
    public ArrayList<CaixaObj> getListPedidos() {
        return pedidos;
    }

    @Override
    public void totalItens() {
        view.checkBoxDezPorcento().setText(R.string.cb_dez_porcento);
        String totalitens = getContext().getText(R.string.total_itens) + " " + pedidos.size();
        view.totalItens().setText(totalitens);
    }

    public void valorTotalComDez() {
        //aqui é somado os precos dos itens da lista.
        valorTotal = 0;
        dezPorcento = true;
        for (int x = 0; x < pedidos.size(); x++) {
            valorTotal += pedidos.get(x).getPreco();
        }
        valorTotal = (float) (valorTotal + (valorTotal * 0.1)); //todo monitorar aqui, regular aqui
        valorTotal -= valorDesconto;
        DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float
        String valor = getContext().getText(R.string.valor_total_com_dez) + " " + getContext().getString(R.string.moeda) + " " + df.format(valorTotal);
        view.valorTotal().setText(valor);
    }

    @Override
    public void valorTotalSemDez() {
        //aqui é somado os precos dos itens da lista.
        valorTotal = 0;
        dezPorcento = false;
        for (int x = 0; x < pedidos.size(); x++) {
            valorTotal += pedidos.get(x).getPreco();
        }
        valorTotal -= valorDesconto;
        DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float
        String valor = getContext().getText(R.string.valor_total_sem_dez) + " " + getContext().getString(R.string.moeda) + " " + df.format(valorTotal);
        view.valorTotal().setText(valor);
    }

    @Override
    public void alertCupom() {
        view.alertCupom(getContext().getString(R.string.dialog_mensagem_caixa_cupom_titulo), getContext().getString(R.string.dialog_mensagem_caixa_cupom_mensagem));
    }

    @Override
    public void consultaCodigoDesconto(String codigo) {
        if (codigo.isEmpty()) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sem_codigo_digitado));
        } else if (descontoUnico) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_codigo_duplicado));
        } else {
            view.setProgressVisibility(true);
            model.consultaCodigoDesconto(codigo);
        }
    }

    @Override
    public void responseCodigoDesconto(Task<DocumentSnapshot> task, String codigo) {
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            if (doc != null && doc.exists()) {
                valorDesconto = Objects.requireNonNull(doc.getDouble("desconto")).floatValue();
                if (dezPorcento) {
                    valorTotalComDez();
                    descontoUnico = true;
                } else {
                    valorTotalSemDez();
                    descontoUnico = true;
                }
                if (Objects.requireNonNull(doc.getDouble("limite")).floatValue() == Objects.requireNonNull(doc.getDouble("quantidade")).floatValue()) {
                    view.setProgressVisibility(false);
                    SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_codigo_expirado));
                } else {
                    model.salvaLimiteDesconto(Objects.requireNonNull(doc.getDouble("limite")).floatValue() + 1, codigo);
                }

            } else {
                view.setProgressVisibility(false);
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_codigo_digitado));
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseLimiteDesconto(Task<Void> task) {
        if (task.isSuccessful()) {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.caixa_codigo_ok));
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void avisoPagamento() {
        view.alertAviso(getContext().getString(R.string.dialog_alerta), getContext().getString(R.string.dialog_alerta_desconto_porcentagem));
    }

    @Override
    public void EscolherFormaPagamento() {
        ArrayList<String> itens = new ArrayList<>();
        itens.add(getContext().getString(R.string.dialog_menu_cartao_credito));
        itens.add(getContext().getString(R.string.dialog_menu_cartao_debito));
        itens.add(getContext().getString(R.string.dialog_menu_cartao_vale));
        itens.add(getContext().getString(R.string.dialog_menu_dinheiro));

        view.alertEscolherFormaPagamento(getContext().getString(R.string.dialog_menu_mensagem), itens);
    }

    @Override
    public void valorPago(String opcaoPagamento) {
        map.put("valortotal", valorTotal);
        map.put("dezporcento", dezPorcento);
        map.put("cupomdesconto", descontoUnico);
        map.put("valordesconto", valorDesconto);
        map.put("formapagescolhido", opcaoPagamento);

        DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float

        view.alertValorPago(opcaoPagamento, "\n" + getContext().getString(R.string.dialog_caixa_valor) + " " + df.format(valorTotal) + "\n\n" + getContext().getString(R.string.dialog_mensagem_valor));
    }

    @Override
    public void cpf(String valorPago) {
        DecimalFormat df = new DecimalFormat("0.00"); //limita 2 casas decimais do float
        if (Integer.valueOf(valorPago.replace(",", "").replace(".", "")) >= Integer.valueOf(df.format(valorTotal).replace(",", "").replace(".", ""))) {
            valorPagoCliente = valorPago;
            view.alertCpf(getContext().getString(R.string.dialog_titulo_cpf), getContext().getString(R.string.dialog_mensagem_cpf));
        } else {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_caixa_valor_menor));
        }
    }

    @Override
    public void uidCaixa(String cpf) {
        if (cpf.isEmpty()) {
            cpf = getContext().getString(R.string.caixa_cpf_nao);
            map.put("cpf", cpf);
        } else {
            map.put("cpf", cpf);
        }

        map.put("uidcaixa", model.obterUidCaixa());
        map.put("data", new Date());

        view.setProgressVisibility(true);
        model.salvaVenda(map);
    }

    @Override
    public void responseVenda(Task<DocumentReference> task) {
        if (task.isSuccessful()) {
            for (int i = 0; i < pedidos.size(); i++) {
                model.mudaStatusPedidos(pedidos.get(i).getUidItem());
                ultimoPedido = i == (pedidos.size() - 1);
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseMudarStatus(Task<Void> task) {
        if (task.isSuccessful()) {
            if (ultimoPedido) {
                ultimoPedido = false;
                for (int i = 0; i < pedidos.size(); i++) {
                    model.excluiPedidoMesa(pedidos.get(i).getUidUser());
                    ultimoPedido = i == (pedidos.size() - 1);
                }
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseExcluiPedidoMesa(Task<QuerySnapshot> task, String uid) {
        if (task.isSuccessful()) {
            ArrayList<String> idPedidos = new ArrayList<>();

            if (task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult()) {
                    idPedidos.add(doc.getId());
                }

                itenspedido = false;
                for (int i = 0; i < idPedidos.size(); i++) {
                    model.excluiItens(idPedidos.get(i), uid);
                    itenspedido = i == (idPedidos.size() - 1);
                }
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseExcluiItens(Task<Void> task, String uid) {
        if (task.isSuccessful()) {
            if (itenspedido) {
                model.excluiUid(uid);
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseExcluiUid(Task<Void> task) {
        if (task.isSuccessful()) {
            if (ultimoPedido) {
                model.buscaValorFechamento();
            }
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseBuscaValorFechamento(Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            if (doc != null) {
                Float valor = Objects.requireNonNull(doc.getDouble("valorfechamento")).floatValue() + valorTotal;
                model.salvaValorFechamento(valor);
            }
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }

    @Override
    public void responseSalvaValorFechamento(Task<Void> task) {
        if (task.isSuccessful()) {
            view.startActivity(valorPagoCliente, valorTotal);
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
        }
    }
}