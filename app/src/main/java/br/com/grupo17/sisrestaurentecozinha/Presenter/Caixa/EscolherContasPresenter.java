package br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class EscolherContasPresenter implements Caixa.EscolherContasPresenter {
    private boolean ultimoPedido, verificador;
    private Caixa.EscolherContasActivity view;
    private ArrayList<CaixaObj> pedidos;
    private List<String> clientesUIDS;
    private List<CaixaObj> clientes;
    private Caixa.ModelCaixa model;
    private int contador;

    public EscolherContasPresenter(Caixa.EscolherContasActivity view) {
        this.view = view;
        this.verificador = false;
        this.pedidos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.clientesUIDS = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public ArrayList<CaixaObj> getListPedidos() {
        return pedidos;
    }

    @Override
    public List<CaixaObj> getListClientes() {
        return clientes;
    }

    @Override
    public List<String> getListClientesUIDS() {
        return clientesUIDS;
    }

    @Override
    public void buscaNomesContas() {
        clientes.clear();
        clientesUIDS.clear();
        model.buscaNomeContas();
        view.setButtonEnabled(false);
        view.setProgressVisibility(true);
    }

    @Override
    public void responseNomesContas(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    clientes.add(new CaixaObj(doc.getId(), doc.getString("nomesobrenome")));
                }
                view.setProgressVisibility(false);
                view.setButtonEnabled(true);
                view.criaLista();
            }
        } else {
            view.setProgressVisibility(false);
            try {
                SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException()).getMessage());
            } catch (NullPointerException e) {
                SpAndToast.showMessage(getContext(), e.getMessage());
            }
        }
    }

    @Override
    public void buscaPedidos() {
        if (clientesUIDS.size() > 0) {
            view.setButtonEnabled(false);
            view.setProgressVisibility(true);
            String mesa = SpAndToast.getSP(getContext(), "mesa");
            for (int i = 0; i < clientesUIDS.size(); i++) {
                model.buscaPedidos(mesa, clientesUIDS.get(i));
                ultimoPedido = i == (clientesUIDS.size() - 1);
            }
        } else {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_sem_selecionar_usuario));
        }
    }

    @Override
    public void responseListPedidos(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    float preco;
                    try {
                        preco = Objects.requireNonNull(doc.getDouble("preco")).floatValue();
                    } catch (NullPointerException e) {
                        preco = 0;
                        Log.d("MAIN", "Preco null, ID: " + doc.getId());
                    }

                    pedidos.add(
                            new CaixaObj(
                                    doc.getString("uid"),
                                    doc.getString("nomesobrenome"),
                                    doc.getId(),
                                    doc.getString("cpf"),
                                    doc.getString("numeropedido"),
                                    doc.getString("pratonome"),
                                    preco
                            )
                    );
                }

                if (ultimoPedido) {
                    for (int x = 0; x < pedidos.size(); x++) {
                        model.verificaPedidosAbertos(pedidos.get(x).getUidItem());
                    }
                }
            }
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), getContext().getString(R.string.erro_consultar)).getMessage());
        }
    }

    @Override
    public void responseVerificaPedidos(Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            if (doc != null) {
                contador += 1;
                if (!Objects.requireNonNull(doc.getString("statusitem"), "Status null").equals("Aguardando Pagamento")) {
                    verificador = true;
                }

                if (contador == pedidos.size()) {
                    if (verificador) {
                        contador = 0;
                        pedidos.clear();
                        verificador = false;
                        view.setButtonEnabled(true);
                        view.setProgressVisibility(false);
                        SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_pedido_aberto));
                    } else {
                        for (int x = 0; x < clientesUIDS.size(); x++) {
                            model.fechaContas(clientesUIDS.get(x));
                            ultimoPedido = x == (clientesUIDS.size() - 1);
                        }
                    }
                }
            }
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), "Erro ao consultar").getMessage());
        }
    }

    @Override
    public void responseListfechaConta(Task<Void> task) {
        if (task.isSuccessful()) {
            if (ultimoPedido) {
                view.startActivity();
            }
        } else {
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), "Erro ao fechar conta").getMessage());
        }
    }
}