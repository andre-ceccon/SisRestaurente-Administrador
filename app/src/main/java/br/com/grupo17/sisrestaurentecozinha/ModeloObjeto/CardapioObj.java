package br.com.grupo17.sisrestaurentecozinha.ModeloObjeto;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.R;

public class CardapioObj implements Parcelable {
    private String id;
    private String pathFoto;
    private String nome;
    private String status;
    private String descricao;
    private String categoria;
    private Float avaliacao;
    private Float preco;
    private Float quantidadeAV;

    public CardapioObj(String id, String pathFoto, String nome, String status, String descricao, String categoria, Float avaliacao, Float preco, Float quantidadeAV) {
        this.id = id;
        this.pathFoto = pathFoto;
        this.nome = nome;
        this.status = status;
        this.descricao = descricao;
        this.categoria = categoria;
        this.avaliacao = avaliacao;
        this.preco = preco;
        this.quantidadeAV = quantidadeAV;
    }

    public CardapioObj(Context context, HashMap<String, String> map) {
        this.nome = map.get(context.getString(R.string.key_campo_nome_prato));
        this.descricao = map.get(context.getString(R.string.key_campo_descricao));
        this.status = map.get(context.getString(R.string.key_campo_statusitem));
        this.pathFoto = map.get(context.getString(R.string.key_campo_foto));
        this.categoria = map.get(context.getString(R.string.key_campo_categoria));
        this.preco = Float.valueOf(Objects.requireNonNull(map.get(context.getString(R.string.key_campo_preco))));
        this.avaliacao = (float) 0;
        this.quantidadeAV = (float) 0;
    }

    public CardapioObj(String pathFoto, String nome, String status, String descricao, String categoria, Float avaliacao, Float preco, Float quantidadeAV) {
        this.pathFoto = pathFoto;
        this.nome = nome;
        this.status = status;
        this.descricao = descricao;
        this.categoria = categoria;
        this.avaliacao = avaliacao;
        this.preco = preco;
        this.quantidadeAV = quantidadeAV;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Float getQuantidadeAV() {
        return quantidadeAV;
    }

    public void setQuantidadeAV(Float quantidadeAV) {
        this.quantidadeAV = quantidadeAV;
    }

    public HashMap<String, Object> getMap(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(context.getString(R.string.key_campo_foto), this.pathFoto);
        map.put(context.getString(R.string.key_campo_nome_prato), this.nome);
        map.put(context.getString(R.string.key_campo_avaliacao), this.avaliacao);
        map.put(context.getString(R.string.key_campo_preco), this.preco);
        map.put(context.getString(R.string.key_campo_statusitem), this.status);
        map.put(context.getString(R.string.key_campo_descricao), this.descricao);
        map.put(context.getString(R.string.key_campo_quantidade_avaliacao), this.quantidadeAV);
        map.put(context.getString(R.string.key_campo_categoria).toLowerCase(), this.categoria);
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.pathFoto);
        dest.writeString(this.nome);
        dest.writeString(this.status);
        dest.writeString(this.descricao);
        dest.writeString(this.categoria);
        dest.writeValue(this.avaliacao);
        dest.writeValue(this.preco);
        dest.writeValue(this.quantidadeAV);
    }

    protected CardapioObj(Parcel in) {
        this.id = in.readString();
        this.pathFoto = in.readString();
        this.nome = in.readString();
        this.status = in.readString();
        this.descricao = in.readString();
        this.categoria = in.readString();
        this.avaliacao = (Float) in.readValue(Float.class.getClassLoader());
        this.preco = (Float) in.readValue(Float.class.getClassLoader());
        this.quantidadeAV = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Parcelable.Creator<CardapioObj> CREATOR = new Parcelable.Creator<CardapioObj>() {
        @Override
        public CardapioObj createFromParcel(Parcel source) {
            return new CardapioObj(source);
        }

        @Override
        public CardapioObj[] newArray(int size) {
            return new CardapioObj[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardapioObj obj = (CardapioObj) o;
        return Objects.equals(id, obj.id) &&
                Objects.equals(pathFoto, obj.pathFoto) &&
                Objects.equals(nome, obj.nome) &&
                Objects.equals(status, obj.status) &&
                Objects.equals(descricao, obj.descricao) &&
                Objects.equals(categoria, obj.categoria) &&
                Objects.equals(avaliacao, obj.avaliacao) &&
                Objects.equals(preco, obj.preco) &&
                Objects.equals(quantidadeAV, obj.quantidadeAV);
    }
}