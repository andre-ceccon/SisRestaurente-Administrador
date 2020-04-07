package br.com.grupo17.sisrestaurentecozinha.Interfaces;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CaixaRelatorio;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Cupom;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.User;

public interface Administracao {
    interface ModelAdministracao {
        void deleteHorario(String dia, String horario, boolean especial, boolean ultimoItem);

        void deletaCupom(String cupom);

        void deletaItemCaixa(String id);

        void retriveDadosRelatorios(Query query);

        void retriveQuantidadeMesa();

        void retriveReserva();

        void retriveHorariosFuncionamentos(String dias);

        void retriveUsuario(String cpf);

        void resetaMesa(String userId);

        void retriveDiasEspeciais();

        void retriveHorarios(String dia);

        void retriveCupons();

        void retriveDadosCaixas();

        CollectionReference getCollectionReferencePedidos();

        void updateQuantidadeMesa(String qtMesa);

        void updateReserva(Map<String, Object> map);

        void updateNivelUser(String idUser, String nivelUser);

        void updateStatusCaixa(String id, String status);

        void salvaHorarios(int configuracao, Boolean finaliza, String dia, HashMap<String, Object> map);

        void salvaCupom(Map<String, Object> map);
    }

    interface HorarioFuncionamentoView extends InterfaceBase.ProgressVisibility {
        void onClickDiaSemana(String dia);

        void onClickDelete(String horario);

        void setSubTitle(String menssagem);

        void setInfoSemRegistro(boolean estado);

        void setVissibilityButtom(boolean vissibility);

        void setRecyclerVisibility(boolean visibility);

        void updateListComHorarios();

        void updateListComDiasSemanas();
    }

    interface GerenciamentoCupomView extends InterfaceBase.ProgressVisibility {
        Map<String, Object> getInformacoes();

        void setErro(String campo, String menssagem);

        void setLayoutListVisibility(boolean visibility);

        void setLayoutCadVisibility(boolean visibility);

        void setVisibilityList(boolean visibility);

        void onClick(Cupom cupom);

        void notifyAdapter();

        void limparCampos();
    }

    interface HorarioEspecialView extends InterfaceBase.ProgressVisibility {
        void setAdapterDias();

        void setAdapterHorario();

        void setSubTitle(String title);

        void onClickDia(String dia);

        void onClickDelete(String horario);

        void setRecyclerVisibility(boolean visibility);

        void setInfoSemRegistro(boolean estado);
    }

    interface CadastroHorariosView extends InterfaceBase.ButtonEnabled {
        void finishAcao();

        void setErro(String mensagem);
    }

    interface RelatoriosVendasView extends InterfaceBase.ButtonEnabled {
        void setErro(String mensagem);

        void setText(Map<String, Object> map);
    }

    interface QuantidadeMesaView extends InterfaceBase.ButtonEnabled {
        void setTextQuantidade(String quantidade);

        void setErro(String erro);

        void limpaCampo();
    }

    interface UsuarioView extends InterfaceBase.ProgressVisibility {
        void setText(User user);

        void setErro(String mgs);

        void limparCampo();

        void setButtonEnabled(int campo, boolean enabled);

        void setLayVisibility(boolean visibility);
    }

    interface ReservaView extends InterfaceBase.ButtonEnabled {
        void setText(String mesa, String pessoas);

        void setErro(int campo, String menssgem);

        void limparCampos();
    }

    interface HorarioFuncionamentoPresenter {

        void deletarHorario(String dia, String horario);

        List<String> getHorariosCadastrados();

        Context getContext();

        void retriveHorarios(String dia);

        void responseHorarios(Task<QuerySnapshot> task);

        void responseDelete(Task<Void> task);
    }

    interface GerenciamentoCupomPresenter {
        void deletaCupom(Cupom cupom);

        Context getContext();

        List<Cupom> getList();

        void retriveCupons();

        void responseSalvaCupom(Task<Void> task);

        void responseRetriveCupons(Task<QuerySnapshot> task);

        void responseDeleteCupon(Task<Void> task);

        void salvarCupom();

        boolean fechaTela();
    }

    interface RelatoriosVendasPresenter {
        void validacaoDados(String dia);

        void responseDados(Task<QuerySnapshot> task);

        Context getContext();

        void retriveDados();
    }

    interface CadastroHorariosPresenter {
        Context getContext();

        List<String> getList();

        void preparaListCadastro();

        void salvaHorarios(String dia, String abertura, String fechamento, String tela, boolean cb_fechado);

        void responseHorarios(Task task);
    }

    interface HorarioEspecialPresenter {
        void deleteHorario(String dia, String horario);

        Context getContext();

        ArrayList<String> getListDias();

        ArrayList<String> getListHorarios();

        void retriveDiasEspeciais();

        void retriveHorarios(String dia);

        void responseDiasEspeciais(Task<QuerySnapshot> task);

        void responseHorarios(Task<QuerySnapshot> task);

        void responseDelete(Task<Void> task);
    }

    interface QuantidadeMesaPresenter {
        Context getContext();

        void alteraQuantidade(String quantidadeMesa);

        void buscaQuantidadeMesa();

        void responseRetriveQuantidade(Task<DocumentSnapshot> task);

        void responseUpdateQtMesa(Task<Void> task);
    }

    interface ReservaPresenter {
        Context getContext();

        void retriveReserva();

        void responseReserva(Task<DocumentSnapshot> task);

        void alteraReserva(String mesa, String pessoas);

        void responseAlterar(Task<Void> task);
    }

    interface UsuarioPresenter {
        Context getContex();

        void retriveUsuario(String cpf);

        void responseUsuario(Task<QuerySnapshot> task);

        void responseUpdate(Task<Void> task);

        void responseReset(Task<Void> task);

        void updateNivel(String nivel);

        void resetaMesa();
    }

    interface CaixaRelatorioView extends InterfaceBase.ButtonEnabled {
        String getIdSetado();

        void onClick(CaixaRelatorio caixaRelatorio);

        void notifyAdapter();

        void semItem(boolean visibility);

        void setVisibillityList(boolean visibility);
    }

    interface CaixaRelatorioPresenter {
        void deletarItem(CaixaRelatorio relatorio);

        Context getContext();

        List<CaixaRelatorio> getList();

        void retriveDadosCaixa();

        void responseDados(QuerySnapshot queryDocumentSnapshots);

        void responseStatus(Task<Void> task);

        void responseDelete(Task<Void> task);

        void updateStatus(CaixaRelatorio relatorio);
    }
}