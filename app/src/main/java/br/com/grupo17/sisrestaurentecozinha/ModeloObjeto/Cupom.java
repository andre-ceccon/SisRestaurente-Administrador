package br.com.grupo17.sisrestaurentecozinha.ModeloObjeto;

public class Cupom {
    private String id;
    private int quantidade;
    private float desconto;
    private int limite;

    public Cupom(String id, int quantidade, float desconto, int limite) {
        this.id = id;
        this.quantidade = quantidade;
        this.desconto = desconto;
        this.limite = limite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }
}