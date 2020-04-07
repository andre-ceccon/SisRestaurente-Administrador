package br.com.grupo17.sisrestaurentecozinha.Presenter.Conta;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Conta;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.User;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class LoginPresenter implements Conta.LoginPresenter {
    private Conta.LoginView loginView;
    private ModelDb model;

    public LoginPresenter(Conta.LoginView loginView) {
        this.loginView = loginView;
        this.model = new ModelDb(this);
    }

    @Override
    public void checkLogin() {
        loginView.isLogin(model.isLoggedIn());
    }

    @Override
    public void deslogar() {
        model.deslogar();
    }

    @Override
    public Context getContext() {
        return (Context) loginView;
    }

    @Override
    public void onSendLogin(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            loginView.loginError(R.string.erro_campo_vazio);
        } else {
            loginView.setButtonEnabled(false);
            loginView.setProgressVisibility(true);
            model.login(email, password);
        }
    }

    @Override
    public void responseLogin(Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            try {
                if (task.getException() != null) {
                    loginView.setProgressVisibility(false);
                    loginView.setButtonEnabled(true);
                    throw task.getException();
                }
            } catch (FirebaseAuthInvalidCredentialsException e) {
                if (e.getMessage().contains("email")) {
                    loginView.loginError(R.string.erro_email_invalido);
                } else if (e.getMessage().contains("password")) {
                    loginView.loginError(R.string.erro_senha_errado);
                }
            } catch (FirebaseAuthInvalidUserException e) {
                loginView.loginError(R.string.erro_email_nao_cadastrado);
            } catch (FirebaseNetworkException e) {
                loginView.loginError(R.string.erro_sem_conexao);
            } catch (Exception e) {
                loginView.loginError(-1);
            }
        } else {
            model.verificaNivelUsuario();
        }
    }

    @Override
    public void responseVerificacaoNivelUser(Task<DocumentSnapshot> task) {
        loginView.setProgressVisibility(false);
        DocumentSnapshot doc = task.getResult();
        if (doc != null && doc.exists()) {
            User user = doc.toObject(User.class);
            if (user != null && (user.getNivelUser().equals(getContext().getString(R.string.key_nivelUser_cozinha)) || user.getNivelUser().equals(getContext().getString(R.string.key_nivelUser_adm)) || user.getNivelUser().equals(getContext().getString(R.string.key_nivelUser_caixa)))) {
                SpAndToast.saveSP(getContext(), getContext().getString(R.string.key_campo_nivelUser), user.getNivelUser());
                loginView.loginSuccess();
            } else {
                model.deslogar();
                loginView.setButtonEnabled(true);
                loginView.loginError(R.string.erro_nivel_usuario);
            }
        } else {
            model.deslogar();
            loginView.loginError(-1);
            loginView.setButtonEnabled(true);
        }
    }
}