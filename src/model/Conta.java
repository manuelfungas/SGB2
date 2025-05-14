package model;


import java.io.Serializable;

public class Conta implements Serializable {
    private final String nib;
    private String nomeCliente;
    private String moeda;
    private double saldoAtual;
    private String codigoAgencia;

    public Conta(String nib, String nomeCliente, String moeda, double saldoAtual, String codigoAgencia) {
        this.nib = nib;
        this.nomeCliente = nomeCliente;
        this.moeda = moeda;
        this.saldoAtual = saldoAtual;
        this.codigoAgencia = codigoAgencia;
    }

    public String getNib() {
        return nib;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public void atualizarSaldo(double valor) {
        this.saldoAtual += valor;
    }

    @Override
    public String toString() {
        return nib + " - " + nomeCliente;
    }
}