package br.com.grupo17.sisrestaurentecozinha.Presenter.Conta;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Conta;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class RecuperaSenhaPresenter implements Conta.RecuperaSenhaPresenter {
    private Conta.RecuperaSenhaView view;
    private ModelDb modelDb;

    public RecuperaSenhaPresenter(Conta.RecuperaSenhaView view) {
        this.view = view;
        this.modelDb = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void responseRecuperar(Task<Void> task) {
        if (task.isSuccessful()) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_recuperado));
            view.setButtonEnabled(true);
            view.sucessoRecuperar();
        } else {
            try {
                if (task.getException() != null) {
                    view.setProgressVisibility(false);
                    view.setButtonEnabled(true);
                    throw task.getException();
                }
            } catch (FirebaseAuthInvalidUserException e) {
                view.setError(getContext().getString(R.string.erro_email_nao_cadastrado));
            } catch (FirebaseAuthInvalidCredentialsException e) {
                view.setError(getContext().getString(R.string.erro_email_invalido));
            } catch (Exception e) {
                view.setError(e.getMessage());
            }
        }
    }

    @Override
    public void sendRecuperar(String email) {
        if (TextUtils.isEmpty(email)) {
            view.setError(getContext().getString(R.string.erro_email_vazio));
        } else {
            view.setButtonEnabled(false);
            view.setProgressVisibility(true);
            modelDb.recuperarSenha(email);
        }
    }
}