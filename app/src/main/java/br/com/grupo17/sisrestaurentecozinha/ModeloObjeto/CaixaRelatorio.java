package br.com.grupo17.sisrestaurentecozinha.ModeloObjeto;

import java.util.Comparator;
import java.util.Date;

public class CaixaRelatorio {
    private String id;
    private String cpf;
    private Date data;
    private String nome;
    private String status;
    private float abertura;
    private float fechamento;

    public CaixaRelatorio(String id, String cpf, Date data, String nome, String status, float abertura, float fechamento) {
        this.id = id;
        this.cpf = cpf;
        this.data = data;
        this.nome = nome;
        this.status = status;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public float getAbertura() {
        return abertura;
    }

    public void setAbertura(float abertura) {
        this.abertura = abertura;
    }

    public float getFechamento() {
        return fechamento;
    }

    public void setFechamento(float fechamento) {
        this.fechamento = fechamento;
    }

    public static Comparator<CaixaRelatorio> NameComparator = new Comparator<CaixaRelatorio>() {
        public int compare(CaixaRelatorio s1, CaixaRelatorio s2) {
            return Integer.compare(s2.getStatus().length(), s1.getStatus().length());
        }
    };
}