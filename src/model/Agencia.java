package model;

import java.io.Serializable;

public class Agencia implements Serializable {
    private final String codigo;
    private String nome;
    private String localizacao;
    private String telefone;

    public Agencia(String codigo, String nome, String localizacao, String telefone) {
        this.codigo = codigo;
        this.nome = nome;
        this.localizacao = localizacao;
        this.telefone = telefone;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}