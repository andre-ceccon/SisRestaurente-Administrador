package br.com.grupo17.sisrestaurentecozinha.ModeloObjeto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

public class Pedido implements Parcelable {
    private String id;
    private Date date;
    private String pratoNome;
    private String pontoCarne;
    private String observacoes;
    private String status;
    private Float preco;

    public Pedido(String id, Date date, String pratoNome, String pontoCarne, String observacoes, String status) {
        this.id = id;
        this.date = date;
        this.pratoNome = pratoNome;
        this.pontoCarne = pontoCarne;
        this.observacoes = observacoes;
        this.status = status;
    }

    public Pedido(String id, Date date, String pratoNome, Float preco) {
        this.id = id;
        this.date = date;
        this.pratoNome = pratoNome;
        this.preco = preco;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPratoNome() {
        return pratoNome;
    }

    public void setPratoNome(String pratoNome) {
        this.pratoNome = pratoNome;
    }

    public String getPontoCarne() {
        return pontoCarne;
    }

    public void setPontoCarne(String pontoCarne) {
        this.pontoCarne = pontoCarne;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public static Comparator<Pedido> dataComparator = new Comparator<Pedido>() {
        public int compare(Pedido s1, Pedido s2) {
            if (s1.getDate() == null || s2.getDate() == null) {
                return 0;
            }
            return s1.getDate().compareTo(s2.getDate());
        }
    };

    //Parcel que é a entidade que será transmitida (para outra Activity, para buffer de utilização do SavedInstanceState, ...), a leitura desses dados que estão no objeto Parcel também é uma das funcionalidades da classe que implementa o Parcelable. Note que o uso da interface Parcelable tende a ser mais eficiente quanto ao transporte dos dados devido a classe Parcel ter sido construída para ter alta performance no empacotamento, transporte e desempacotamento dos dados. Mas é isso, o uso é bem tranquilo depois que nós vemos a primeira vez.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.pratoNome);
        dest.writeString(this.pontoCarne);
        dest.writeString(this.observacoes);
        dest.writeString(this.status);
        dest.writeValue(this.preco);
    }

    private Pedido(Parcel in) {
        this.id = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.pratoNome = in.readString();
        this.pontoCarne = in.readString();
        this.observacoes = in.readString();
        this.status = in.readString();
        this.preco = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<Pedido> CREATOR = new Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel source) {
            return new Pedido(source);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };
}