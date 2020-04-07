package br.com.grupo17.sisrestaurentecozinha.Presenter.Administracao.Configuracoes;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Model.ModelDb;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.Connectivity;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class CadastrosHorariosPresenter implements Administracao.CadastroHorariosPresenter {
    private Administracao.CadastroHorariosView view;
    private List<String> horariosParaCadastro;
    private ModelDb model;

    public CadastrosHorariosPresenter(Administracao.CadastroHorariosView view) {
        this.view = view;
        this.horariosParaCadastro = new ArrayList<>();
        this.model = new ModelDb(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public List<String> getList() {
        return horariosParaCadastro;
    }

    private void getException(Task task) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (Exception e) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_ao_salvar_item));
        }
    }

    public void preparaListCadastro() {
        if (horariosParaCadastro.size() == 0) {
            horariosParaCadastro.add(getContext().getString(R.string.campo_hora));
            int hora = 0;
            do {
                if (hora < 10) {
                    horariosParaCadastro.add("0" + String.valueOf(hora) + ":00");
                    horariosParaCadastro.add("0" + String.valueOf(hora) + ":30");
                } else {
                    horariosParaCadastro.add(String.valueOf(hora) + ":00");
                    horariosParaCadastro.add(String.valueOf(hora) + ":30");
                }
                hora++;
            } while (hora != 24);
        }
    }

    @Override
    public void salvaHorarios(String dia, String aberto, String fechado, String tela, boolean cb_fechado) {
        HashMap<String, Object> map = new HashMap<>();
        if (cb_fechado) {
            if (validaData(tela, dia)) {
                map.put(getContext().getString(R.string.campo_hora), getContext().getString(R.string.campo_hora_fechado));
                view.setButtonEnabled(false);
                view.setProgressVisibility(true);
                model.salvaHorarios(R.string.key_path_db_horarios_espaciais, true, dia, map);
            }
        } else if (aberto.equals(getContext().getString(R.string.campo_hora))) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_horario_abertura_vazio));
        } else if (fechado.equals(getContext().getString(R.string.campo_hora))) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_horario_fechamento_vazio));
        } else if (!Connectivity.isConnected(getContext())) {
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_sem_conexao));
        } else if (validaData(tela, dia)) {
            int abertura = horariosParaCadastro.indexOf(aberto);
            int fechamento = horariosParaCadastro.indexOf(fechado);

            if (abertura == fechamento) {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_horarios_iguais));
            } else if (abertura > fechamento) {
                SpAndToast.showMessage(getContext(), getContext().getString(R.string.erro_horario_fechamento_anterios_abertura));
            } else {
                view.setButtonEnabled(false);
                view.setProgressVisibility(true);
                int string = tela.equals(getContext().getString(R.string.key_tela_especial)) ? R.string.key_path_db_horarios_espaciais : R.string.key_path_db_horario_funcionamento;
                for (int i = abertura; i <= fechamento; i++) {
                    map.put(getContext().getString(R.string.campo_hora), horariosParaCadastro.get(i));
                    model.salvaHorarios(string, i == fechamento, dia, map);
                }
            }
        }
    }

    private boolean validaData(String tela, String dia) {
        if (tela.equals(getContext().getString(R.string.key_tela_especial))) {
            if (TextUtils.isEmpty(dia)) {
                view.setErro(getContext().getString(R.string.erro_data_vazia));
            } else if (dia.length() < 9) {
                view.setErro(getContext().getString(R.string.erro_data_incompleta));
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
                dateFormat.setLenient(false); // aqui o pulo do gato
                try {
                    dateFormat.parse(dia);
                    return true;
                } catch (ParseException ex) {
                    view.setErro(getContext().getString(R.string.erro_data_invalida));
                }
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void responseHorarios(Task task) {
        if (!task.isSuccessful()) {
            getException(task);
        } else {
            view.setButtonEnabled(true);
            view.setProgressVisibility(false);
            SpAndToast.showMessage(getContext(), getContext().getString(R.string.aviso_sucesso_cadastro));
            view.finishAcao();
        }
    }
}