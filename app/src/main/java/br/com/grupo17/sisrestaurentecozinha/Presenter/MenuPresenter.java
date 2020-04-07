package br.com.grupo17.sisrestaurentecozinha.Presenter;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.MenuInter;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class MenuPresenter implements MenuInter.MenuPresenter {
    private MenuInter.MenuView view;
    private ModelDb model;

    public MenuPresenter(MenuInter.MenuView view) {
        this.view = view;
        this.model = new ModelDb(this);
    }

    @Override
    public void deslogar() {
        model.deslogar();
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void nivelUserRealTime() {
        model.nivelUserRealTime();
    }

    @Override
    public void responseNivelUser(Activity activity, DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null && documentSnapshot.exists()) {
            String nivel = Objects.requireNonNull(
                    documentSnapshot.get("nivelUser"),
                    "erro")
                    .toString();

            SpAndToast.saveSP(getContext(), getContext().getString(R.string.key_campo_nivelUser), nivel);

            if (nivel.equals("banido") || nivel.equals("cliente") || nivel.equals("garçom")) {
               view.fechaActivity("Usuário não é mais autorizado");
            }
        }
    }
}