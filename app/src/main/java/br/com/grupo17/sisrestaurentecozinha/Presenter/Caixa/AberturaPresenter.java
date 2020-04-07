package br.com.grupo17.sisrestaurentecozinha.Presenter.Caixa;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class AberturaPresenter implements Caixa.AberturaPresenter {
    private Map<String, Object> abertura;
    private Caixa.AberturaActivity view;
    private ModelDb model;
    private String valor;

    public AberturaPresenter(Caixa.AberturaActivity view) {
        this.view = view;
        this.abertura = new HashMap<>();
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void verificaCaixaAberto() {
        view.setProgressVisibility(true);
        view.setButtonEnabled(false);
        model.verificaCaixaAberto();
    }

    @Override
    public void responseVerificaCaixaAberto(Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            if (doc != null) {
                if (doc.exists()) {
                    if (Objects.requireNonNull(doc.getString("status"), "").contains("fechado")) {
                        SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_abertura_fechado));
                        view.startActivityMenu();
                    } else if (Objects.requireNonNull(doc.getString("status"), "").contains("aguardando conferencia")) {
                        SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_abertura_aguardando));
                        view.startActivityMenu();
                    } else {
                        view.startActivity();
                    }
                } else {
                    view.setButtonEnabled(true);
                    view.setProgressVisibility(false);
                }
            }
        } else {
            view.setButtonEnabled(true);
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), getContext().getString(R.string.erro_consultar)).getMessage());
        }
    }

    @Override
    public void aberturaCaixa(String valor) {
        view.setProgressVisibility(true);
        view.setButtonEnabled(false);
        if (valor.isEmpty()) {
            view.mesaErros(R.string.erro_caixa_abertura_vazio);
        } else {
            view.setProgressVisibility(true);
            this.valor = valor;
            model.buscaDadosCaixa();
        }
    }

    @Override
    public void responseBuscaUidCaixa(Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            abertura.put("status", "aguardando conferencia");
            abertura.put("data", new Date());
            abertura.put("valorabertura", Float.parseFloat(valor.replace(",", ".")));
            abertura.put("valorfechamento", 0);
            abertura.put("nomesobrenome", Objects.requireNonNull(doc).getString("nome") + " " + doc.getString("sobrenome"));
            abertura.put("cpf", Objects.requireNonNull(doc.getString("cpf"), ""));

            model.abreCaixa(abertura);
        } else {
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), getContext().getString(R.string.erro_consultar)).getMessage());
        }
    }

    @Override
    public void responseAbreCaixa(Task<Void> task) {
        if (task.isSuccessful()) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_caixa_abertura_aguardando));
            view.startActivityMenu();
        } else {
            view.setButtonEnabled(true);
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), Objects.requireNonNull(task.getException(), getContext().getString(R.string.erro_consultar)).getMessage());
        }
    }
}
