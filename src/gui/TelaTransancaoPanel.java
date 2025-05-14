// TelaTransacaoPanel.java
package gui;

import model.Conta;
import model.GerenciadorDados;
import model.Transacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaTransacaoPanel extends JPanel {
    private GerenciadorDados gerenciador;

    // Componentes da interface
    private JTextField txtNumeroTransacao;
    private JComboBox<String> cmbTipoTransacao;
    private JComboBox<Conta> cmbConta;
    private JTextField txtValor;
    private JLabel lblTaxa;
    private JLabel lblInfoConta;
    private JTable tblTransacoes;
    private DefaultTableModel tableModel;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public TelaTransacaoPanel(GerenciadorDados gerenciador) {
        this.gerenciador = gerenciador;
        setupUI();
        carregarDados();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Transação"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Número da Transação
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Número da Transação:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNumeroTransacao = new JTextField(10);
        formPanel.add(txtNumeroTransacao, gbc);

        // Tipo de Transação
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tipo de Transação:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        cmbTipoTransacao = new JComboBox<>(new String[]{"D - Depósito", "L - Levantamento"});
        formPanel.add(cmbTipoTransacao, gbc);

        // Conta
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Conta:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        cmbConta = new JComboBox<>();
        formPanel.add(cmbConta, gbc);

        // Informações da conta
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        lblInfoConta = new JLabel("Informações da conta: ");
        formPanel.add(lblInfoConta, gbc);
        gbc.gridwidth = 1;

        // Valor
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        txtValor = new JTextField(10);
        formPanel.add(txtValor, gbc);

        // Taxa
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Taxa Aplicável:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        lblTaxa = new JLabel("0.0%");
        formPanel.add(lblTaxa, gbc);

        // Painel de botões do formulário
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrar = new JButton("Registrar Transação");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnCancelar);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Últimas Transações"));

        // Configuração da tabela
        String[] columnNames = {"Número", "Tipo", "Valor", "Taxa", "Conta", "Data"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta na tabela
            }
        };

        tblTransacoes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTransacoes);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Adicionar os painéis ao layout principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Configurar os eventos dos botões
        btnRegistrar.addActionListener(this::handleRegistrarTransacao);
        btnCancelar.addActionListener(this::handleCancelar);

        // Adicionar event listeners para atualizar informações
        cmbConta.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                atualizarInformacoesConta();
            }
        });

        cmbTipoTransacao.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                atualizarTaxa();
            }
        });
    }

    /**
     * Carrega os dados iniciais no formulário
     */
    private void carregarDados() {
        // Limpar e carregar as contas no combobox
        cmbConta.removeAllItems();
        for (Conta conta : gerenciador.getContas()) {
            cmbConta.addItem(conta);
        }

        // Gerar próximo número de transação
        int proximoNumero = gerenciador.getProximoNumeroTransacao();
        txtNumeroTransacao.setText(String.valueOf(proximoNumero));
        txtNumeroTransacao.setEditable(false);

        // Atualizar tabela de transações
        atualizarTabelaTransacoes();

        // Atualizar informações da conta selecionada
        if (cmbConta.getItemCount() > 0) {
            atualizarInformacoesConta();
            atualizarTaxa();
        }
    }

    /**
     * Atualiza as informações da conta selecionada
     */
    private void atualizarInformacoesConta() {
        Conta contaSelecionada = (Conta) cmbConta.getSelectedItem();
        if (contaSelecionada != null) {
            String info = String.format("Informações da conta: Número %s, Saldo: %.2f€",
                    contaSelecionada.getNumero(), contaSelecionada.getSaldo());
            lblInfoConta.setText(info);
            atualizarTaxa();
        } else {
            lblInfoConta.setText("Informações da conta: Nenhuma conta selecionada");
        }
    }

    /**
     * Atualiza a taxa aplicável com base no tipo de transação e tipo de conta
     */
    private void atualizarTaxa() {
        Conta contaSelecionada = (Conta) cmbConta.getSelectedItem();
        String tipoTransacao = (String) cmbTipoTransacao.getSelectedItem();

        if (contaSelecionada != null && tipoTransacao != null) {
            double taxa = 0.0;

            // Verificar o tipo de transação (D - Depósito, L - Levantamento)
            boolean isLevantamento = tipoTransacao.startsWith("L");

            // Calcular taxa conforme regras de negócio (exemplo)
            if (isLevantamento) {
                // Taxa para levantamentos baseada no tipo de conta
                switch (contaSelecionada.getTipo()) {
                    case "Normal":
                        taxa = 0.5; // 0.5%
                        break;
                    case "Premium":
                        taxa = 0.2; // 0.2%
                        break;
                    default:
                        taxa = 1.0; // Taxa padrão
                }
            }

            lblTaxa.setText(String.format("%.1f%%", taxa));
        } else {
            lblTaxa.setText("0.0%");
        }
    }

    /**
     * Atualiza a tabela de transações com os dados mais recentes
     */
    private void atualizarTabelaTransacoes() {
        // Limpar a tabela
        tableModel.setRowCount(0);

        // Obter as transações do gerenciador
        List<Transacao> transacoes = gerenciador.getTransacoes();

        // Formato para a data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Adicionar linhas à tabela (limitando às 15 mais recentes)
        int startIndex = Math.max(0, transacoes.size() - 15);
        for (int i = startIndex; i < transacoes.size(); i++) {
            Transacao t = transacoes.get(i);
            String tipoStr = t.getTipo().equals("D") ? "Depósito" : "Levantamento";

            Object[] rowData = {
                    t.getNumero(),
                    tipoStr,
                    String.format("%.2f€", t.getValor()),
                    String.format("%.1f%%", t.getTaxa()),
                    t.getConta().getNumero(),
                    dateFormat.format(t.getData())
            };

            tableModel.addRow(rowData);
        }
    }

    /**
     * Manipula o evento de clique no botão Registrar Transação
     */
    private void handleRegistrarTransacao(ActionEvent e) {
        try {
            // Validar entradas
            if (cmbConta.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter valores do formulário
            int numeroTransacao = Integer.parseInt(txtNumeroTransacao.getText());
            Conta conta = (Conta) cmbConta.getSelectedItem();
            String tipoTransacaoCompleto = (String) cmbTipoTransacao.getSelectedItem();
            String tipoTransacao = tipoTransacaoCompleto.substring(0, 1); // "D" ou "L"

            // Validar e obter valor
            double valor;
            try {
                valor = Double.parseDouble(txtValor.getText().replace(",", "."));
                if (valor <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Insira um número positivo.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter taxa
            double taxa = Double.parseDouble(lblTaxa.getText().replace("%", ""));

            // Verificar saldo para levantamentos
            if (tipoTransacao.equals("L") && valor > conta.getSaldo()) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente para esta operação.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar e registrar a transação
            Transacao transacao = new Transacao(numeroTransacao, tipoTransacao, valor, taxa/100, conta, new Date());
            boolean sucesso = gerenciador.registrarTransacao(transacao);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Transação registrada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Atualizar a interface
                atualizarTabelaTransacoes();
                atualizarInformacoesConta();

                // Limpar campos para nova transação
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar transação.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao processar transação: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Manipula o evento de clique no botão Cancelar
     */
    private void handleCancelar(ActionEvent e) {
        limparCampos();
    }

    /**
     * Limpa os campos do formulário e prepara para nova transação
     */
    private void limparCampos() {
        // Gerar próximo número de transação
        int proximoNumero = gerenciador.getProximoNumeroTransacao();
        txtNumeroTransacao.setText(String.valueOf(proximoNumero));

        // Redefinir campos
        cmbTipoTransacao.setSelectedIndex(0);
        txtValor.setText("");

        // Manter a conta selecionada mas atualizar as informações
        atualizarInformacoesConta();
    }
}