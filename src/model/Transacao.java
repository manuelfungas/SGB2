package model;

import java.io.Serializable;
import java.util.Date;

public class Transacao implements Serializable {
    private final String numeroTransacao;
    private final String tipoTransacao; // "D" para depósito, "L" para levantamento
    private final double valorTransacao;
    private final double taxa;
    private final String nibConta;
    private final Date dataTransacao;

    public Transacao(String numeroTransacao, String tipoTransacao, double valorTransacao, double taxa, String nibConta) {
        this.numeroTransacao = numeroTransacao;
        this.tipoTransacao = tipoTransacao;
        this.valorTransacao = valorTransacao;
        this.taxa = taxa;
        this.nibConta = nibConta;
        this.dataTransacao = new Date();
    }

    public String getNumeroTransacao() {
        return numeroTransacao;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public double getValorTransacao() {
        return valorTransacao;
    }

    public double getTaxa() {
        return taxa;
    }

    public String getNibConta() {
        return nibConta;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public static double calcularTaxa(String tipoTransacao, String moeda) {
        // 0.5% para levantamentos
        // 0.25% para depósitos em moeda estrangeira
        if ("L".equals(tipoTransacao)) {
            return 0.005; // 0.5%
        } else if ("D".equals(tipoTransacao) && !"MZN".equals(moeda)) {
            return 0.0025; // 0.25%
        }
        return 0.0; // Sem taxa para depósitos em moeda local
    }
}