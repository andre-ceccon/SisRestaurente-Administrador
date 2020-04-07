package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Relatorios;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Pedido;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.RelatoriosUtil;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class RelatorioVendasVendasPresenter implements Administracao.RelatoriosVendasPresenter {
    private Administracao.ModelAdministracao modeldb;
    private DocumentSnapshot documentSnapshot;
    private Administracao.RelatoriosVendasView view;
    private RelatoriosUtil relatoriosUtil;
    private boolean isNew, reRetrive;
    private Calendar calendar;

    public RelatorioVendasVendasPresenter(Administracao.RelatoriosVendasView view) {
        this.view = view;
        this.isNew = true;
        this.reRetrive = false;
        this.calendar = Calendar.getInstance();
        this.modeldb = new ModelDb(this);
        this.relatoriosUtil = new RelatoriosUtil(this);
    }

    @Override
    public void validacaoDados(String dias) {
        if (TextUtils.isEmpty(dias)) {
            view.setErro(getContext().getString(R.string.erro_sem_numero));
        } else {
            int nDias = Integer.valueOf(dias);
            if (nDias >= 1 && nDias <= 365) {
                view.setButtonEnabled(false);
                view.setProgressVisibility(true);
                if (!isNew) {
                    //resetando valores quando é clicado para uma nova consulta
                    isNew = true;
                    calendar = Calendar.getInstance();
                    relatoriosUtil.resetInfo();
                }

                calendar.add(Calendar.DATE, -nDias); //volta no tempo, com base no numero que foi digitado.

                retriveDados();
            } else {
                view.setErro(getContext().getString(R.string.erro_quantidade_de_dias_invalido));
            }
        }
    }

    private Query criaQuery() {
        if (isNew) {
            isNew = false;
            return modeldb.getCollectionReferencePedidos()
                    .orderBy(getContext().getString(R.string.key_campo_data))
                    .startAt(calendar.getTime())
                    .limit(10);
        } else {
            return modeldb.getCollectionReferencePedidos()
                    .orderBy(getContext().getString(R.string.key_campo_data))
                    .startAfter(documentSnapshot)
                    .endAt(calendar.getTime())
                    .limit(10);
        }
    }

    @Override
    public void retriveDados() {
        modeldb.retriveDadosRelatorios(criaQuery()); //busca dados.
    }

    @Override
    public void responseDados(Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            if (!reRetrive) {
                reRetrive = true;
                retriveDados();
            } else {
                reRetrive = false;
                //todo getException
            }
        } else {
            if (task.getResult() != null) {
                reRetrive = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Pedido temp = toObject(document);
                    if (temp != null) {
                        relatoriosUtil.addItemList(toObject(document));
                    } else {
                        SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_item_relatorio));
                    }
                }

                if (task.getResult().size() != 0) {
                    documentSnapshot = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    relatoriosUtil.geraInfos();
                } else {
                    view.setButtonEnabled(true);
                    view.setProgressVisibility(false);

                    Map<String, Object> map = relatoriosUtil.getInfo();
                    calendar.add(Calendar.DATE, +1);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    map.put(getContext().getString(R.string.key_campo_data), simpleDateFormat.format(calendar.getTime()) + " - " + simpleDateFormat.format(new Date()));
                    view.setText(map);
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    private Pedido toObject(QueryDocumentSnapshot dc) {
        try {
            return new Pedido(
                    dc.getId(),
                    dc.getDate(getContext().getString(R.string.key_campo_data)),
                    Objects.requireNonNull(dc.get(getContext().getString(R.string.key_campo_nome_prato))).toString(),
                    Objects.requireNonNull(dc.getDouble(getContext().getString(R.string.key_campo_preco))).floatValue()
            );
        } catch (NullPointerException e) {
            return null;
        }
    }
}