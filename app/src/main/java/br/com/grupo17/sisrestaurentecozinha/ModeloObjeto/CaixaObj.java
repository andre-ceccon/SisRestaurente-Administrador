package br.com.grupo17.sisrestaurentecozinha.ModeloObjeto;

import android.os.Parcel;
import android.os.Parcelable;

public class CaixaObj implements Parcelable {
    private String uidUser;
    private String nomeSobrenome;
    private String uidItem;
    private String cpf;
    private String numeroPedido;
    private String pratoNome;
    private Float preco;

    public CaixaObj() {
    }

    public CaixaObj(String uidUser, String nomeSobrenome) {
        this.uidUser = uidUser;
        this.nomeSobrenome = nomeSobrenome;
    }

    public CaixaObj(String uidUser, String nomeSobrenome, String uidItem, String cpf, String numeroPedido, String pratoNome, Float preco) {
        this.uidUser = uidUser;
        this.nomeSobrenome = nomeSobrenome;
        this.uidItem = uidItem;
        this.cpf = cpf;
        this.numeroPedido = numeroPedido;
        this.pratoNome = pratoNome;
        this.preco = preco;
    }

    private CaixaObj(Parcel in) {
        uidUser = in.readString();
        nomeSobrenome = in.readString();
        uidItem = in.readString();
        cpf = in.readString();
        numeroPedido = in.readString();
        pratoNome = in.readString();
        if (in.readByte() == 0) {
            preco = null;
        } else {
            preco = in.readFloat();
        }
    }

    public static final Creator<CaixaObj> CREATOR = new Creator<CaixaObj>() {
        @Override
        public CaixaObj createFromParcel(Parcel in) {
            return new CaixaObj(in);
        }

        @Override
        public CaixaObj[] newArray(int size) {
            return new CaixaObj[size];
        }
    };

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getNomeSobrenome() {
        return nomeSobrenome;
    }

    public void setNomeSobrenome(String nomeSobrenome) {
        this.nomeSobrenome = nomeSobrenome;
    }

    public String getUidItem() {
        return uidItem;
    }

    public void setUidItem(String uidItem) {
        this.uidItem = uidItem;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getPratoNome() {
        return pratoNome;
    }

    public void setPratoNome(String pratoNome) {
        this.pratoNome = pratoNome;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidUser);
        dest.writeString(nomeSobrenome);
        dest.writeString(uidItem);
        dest.writeString(cpf);
        dest.writeString(numeroPedido);
        dest.writeString(pratoNome);
        if (preco == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(preco);
        }
    }
}