package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorDados implements Serializable {
    private List<Conta> contas;
    private List<Agencia> agencias;
    private List<Transacao> transacoes;
    private static final String ARQUIVO_DADOS = "dadosBancarios.ser";

    public GerenciadorDados() {
        this.contas = new ArrayList<>();
        this.agencias = new ArrayList<>();
        this.transacoes = new ArrayList<>();
        carregarDados();
    }

    // Métodos para Contas
    public boolean adicionarConta(Conta conta) {
        if (buscarContaPorNIB(conta.getNib()) == null) {
            contas.add(conta);
            salvarDados();
            return true;
        }
        return false;
    }

    public boolean atualizarConta(Conta contaAtualizada) {
        for (int i = 0; i < contas.size(); i++) {
            if (contas.get(i).getNib().equals(contaAtualizada.getNib())) {
                contas.set(i, contaAtualizada);
                salvarDados();
                return false;
            }
        }
        return false;
    }

    public Conta buscarContaPorNIB(String nib) {
        return contas.stream()
                .filter(c -> c.getNib().equals(nib))
                .findFirst()
                .orElse(null);
    }

    public List<Conta> getTodasContas() {
        return new ArrayList<>(contas);
    }

    // Métodos para Agências
    public boolean adicionarAgencia(Agencia agencia) {
        if (buscarAgenciaPorCodigo(agencia.getCodigo()) == null) {
            agencias.add(agencia);
            salvarDados();
            return true;
        }
        return false;
    }

    public boolean atualizarAgencia(Agencia agenciaAtualizada) {
        for (int i = 0; i < agencias.size(); i++) {
            if (agencias.get(i).getCodigo().equals(agenciaAtualizada.getCodigo())) {
                agencias.set(i, agenciaAtualizada);
                salvarDados();
                return true;
            }
        }
        return false;
    }

    public Agencia buscarAgenciaPorCodigo(String codigo) {
        return agencias.stream()
                .filter(a -> a.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Agencia> getTodasAgencias() {
        return new ArrayList<>(agencias);
    }

    // Métodos para Transações
    public boolean adicionarTransacao(Transacao transacao) {
        Conta conta = buscarContaPorNIB(transacao.getNibConta());
        if (conta != null) {
            // Verifica se é um levantamento e se há saldo suficiente
            if ("L".equals(transacao.getTipoTransacao())) {
                double valorTotal = transacao.getValorTransacao() + (transacao.getValorTransacao() * transacao.getTaxa());
                if (conta.getSaldoAtual() < valorTotal) {
                    return false; // Saldo insuficiente
                }
                conta.atualizarSaldo(-valorTotal);
            } else {
                // Para depósitos
                double valorLiquido = transacao.getValorTransacao() - (transacao.getValorTransacao() * transacao.getTaxa());
                conta.atualizarSaldo(valorLiquido);
            }

            transacoes.add(transacao);
            atualizarConta(conta);
            salvarDados();
            return true;
        }
        return false;
    }

    public List<Transacao> getTodasTransacoes() {
        return new ArrayList<>(transacoes);
    }

    public List<Transacao> getTransacoesPorConta(String nib) {
        return transacoes.stream()
                .filter(t -> t.getNibConta().equals(nib))
                .collect(Collectors.toList());
    }

    // Métodos para relatórios
    public double getTotalDepositosPorConta(String nib) {
        return getTransacoesPorConta(nib).stream()
                .filter(t -> "D".equals(t.getTipoTransacao()))
                .mapToDouble(Transacao::getValorTransacao)
                .sum();
    }

    public double getTotalLevantamentosPorConta(String nib) {
        return getTransacoesPorConta(nib).stream()
                .filter(t -> "L".equals(t.getTipoTransacao()))
                .mapToDouble(Transacao::getValorTransacao)
                .sum();
    }

    public double getTotalTaxasCobradas() {
        return transacoes.stream()
                .mapToDouble(t -> t.getValorTransacao() * t.getTaxa())
                .sum();
    }

    public Conta getClienteMaiorSaldo() {
        return contas.stream()
                .max(Comparator.comparingDouble(Conta::getSaldoAtual))
                .orElse(null);
    }

    // Persistência de dados
    public void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public void carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                GerenciadorDados dadosCarregados = (GerenciadorDados) ois.readObject();
                this.contas = dadosCarregados.contas;
                this.agencias = dadosCarregados.agencias;
                this.transacoes = dadosCarregados.transacoes;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
                // Inicializa com dados de exemplo se não conseguir carregar
                inicializarDadosExemplo();
            }
        } else {
            // Inicializa com dados de exemplo se o arquivo não existir
            inicializarDadosExemplo();
        }
    }

    private void inicializarDadosExemplo() {
        // Agências de exemplo
        agencias.add(new Agencia("AG001", "Agência Central", "Maputo", "21123456"));
        agencias.add(new Agencia("AG002", "Agência Norte", "Nampula", "26123456"));

        // Contas de exemplo
        contas.add(new Conta("NIB001", "João Silva", "MZN", 5000.0, "AG001"));
        contas.add(new Conta("NIB002", "Maria Costa", "USD", 1000.0, "AG001"));
        contas.add(new Conta("NIB003", "Carlos Santos", "EUR", 2000.0, "AG002"));

        // Transações de exemplo
        transacoes.add(new Transacao("T001", "D", 1000.0, 0.0, "NIB001"));
        transacoes.add(new Transacao("T002", "L", 500.0, 0.005, "NIB001"));
        transacoes.add(new Transacao("T003", "D", 500.0, 0.0025, "NIB002"));

        salvarDados();
    }
}