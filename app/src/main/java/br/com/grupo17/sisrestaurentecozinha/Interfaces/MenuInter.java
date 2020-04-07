package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;

public interface MenuInter {
    interface ModelMenu {
        void nivelUserRealTime();
    }

    interface MenuView {
        void fechaActivity(String mgs);
    }

    interface MenuPresenter {
        void deslogar();

        Context getContext();

        void nivelUserRealTime();

        void responseNivelUser(Activity activity, DocumentSnapshot documentSnapshot);
    }
}