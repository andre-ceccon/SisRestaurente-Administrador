package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

public interface Conta {
    interface ModelConta {
        void deslogar();

        boolean isLoggedIn();

        void login(String email, String password);

        void recuperarSenha(String email);

        void verificaNivelUsuario();
    }

    interface LoginView extends InterfaceBase.ButtonEnabled {
        void isLogin(boolean isLogin);

        void loginSuccess();

        void loginError(int i);
    }

    interface LoginPresenter {
        void checkLogin();

        void deslogar();

        Context getContext();

        void onSendLogin(String email, String password);

        void responseLogin(Task<AuthResult> task);

        void responseVerificacaoNivelUser(Task<DocumentSnapshot> task);
    }

    interface RecuperaSenhaView extends InterfaceBase.ButtonEnabled {
        void setError(String mensagem);

        void sucessoRecuperar();
    }

    interface RecuperaSenhaPresenter {
        Context getContext();

        void sendRecuperar(String email);

        void responseRecuperar(Task<Void> task);
    }
}