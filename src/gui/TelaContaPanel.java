// TelaContaPanel.java
package gui;

import model.Agencia;
import model.Conta;
import model.GerenciadorDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaContaPanel extends JPanel {
    private GerenciadorDados gerenciador;

    // Componentes da interface
    private JTextField txtNIB;
    private JTextField txtNomeCliente;
    private JComboBox<String> cmbMoeda;
    private JTextField txtSaldoInicial;
    private JComboBox<Agencia> cmbAgencia;
    private JTable tblContas;
    private DefaultTableModel tableModel;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnCancelar;

    // Controle de estado
    private boolean editando = false;

    public TelaContaPanel(GerenciadorDados gerenciador) {
        this.gerenciador = gerenciador;
        setupUI();
        carregarDados();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Conta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // NIB
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("NIB:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNIB = new JTextField(20);
        formPanel.add(txtNIB, gbc);

        // Nome do Cliente
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nome do Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtNomeCliente = new JTextField(30);
        formPanel.add(txtNomeCliente, gbc);

        // Moeda
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Moeda:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        cmbMoeda = new JComboBox<>(new String[]{"MZN", "USD", "EUR", "ZAR"});
        formPanel.add(cmbMoeda, gbc);

        // Saldo Inicial
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Saldo Inicial:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        txtSaldoInicial = new JTextField(10);
        formPanel.add(txtSaldoInicial, gbc);

        // Agência
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Agência:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        cmbAgencia = new JComboBox<>();
        formPanel.add(cmbAgencia, gbc);

        // Painel de botões do formulário
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnNovo = new JButton("Nova Conta");
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Contas Cadastradas"));

        // Configuração da tabela
        String[] columnNames = {"NIB", "Nome do Cliente", "Moeda", "Saldo Atual", "Agência"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta na tabela
            }
        };

        tblContas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblContas);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Adicionar os painéis ao layout principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Configurar os eventos dos botões
        btnNovo.addActionListener(this::handleNovo);
        btnSalvar.addActionListener(this::handleSalvar);
        btnCancelar.addActionListener(this::handleCancelar);

        tblContas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblContas.getSelectedRow() != -1) {
                carregarContaSelecionada();
            }
        });

        // Estado inicial
        habilitarCampos(false);
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    private void carregarDados() {
        // Carregar agências no combo
        cmbAgencia.removeAllItems();
        for (Agencia agencia : gerenciador.getTodasAgencias()) {
            cmbAgencia.addItem(agencia);
        }

        // Carregar tabela de contas
        tableModel.setRowCount(0);
        for (Conta conta : gerenciador.getTodasContas()) {
            Agencia agencia = gerenciador.buscarAgenciaPorCodigo(conta.getCodigoAgencia());
            String nomeAgencia = agencia != null ? agencia.getNome() : "";

            tableModel.addRow(new Object[]{
                    conta.getNib(),
                    conta.getNomeCliente(),
                    conta.getMoeda(),
                    conta.getSaldoAtual(),
                    nomeAgencia
            });
        }
    }

    private void habilitarCampos(boolean habilitar) {
        txtNIB.setEnabled(habilitar);
        txtNomeCliente.setEnabled(habilitar);
        cmbMoeda.setEnabled(habilitar);
        txtSaldoInicial.setEnabled(habilitar);
        cmbAgencia.setEnabled(habilitar);
    }

    private void limparCampos() {
        txtNIB.setText("");
        txtNomeCliente.setText("");
        cmbMoeda.setSelectedIndex(0);
        txtSaldoInicial.setText("0.0");
        if (cmbAgencia.getItemCount() > 0) {
            cmbAgencia.setSelectedIndex(0);
        }
    }

    private void carregarContaSelecionada() {
        int selectedRow = tblContas.getSelectedRow();
        if (selectedRow >= 0) {
            String nib = (String) tableModel.getValueAt(selectedRow, 0);
            Conta conta = gerenciador.buscarContaPorNIB(nib);

            if (conta != null) {
                txtNIB.setText(conta.getNib());
                txtNomeCliente.setText(conta.getNomeCliente());
                cmbMoeda.setSelectedItem(conta.getMoeda());
                txtSaldoInicial.setText(String.valueOf(conta.getSaldoAtual()));

                for (int i = 0; i < cmbAgencia.getItemCount(); i++) {
                    Agencia agencia = (Agencia) cmbAgencia.getItemAt(i);
                    if (agencia.getCodigo().equals(conta.getCodigoAgencia())) {
                        cmbAgencia.setSelectedIndex(i);
                        break;
                    }
                }

                editando = true;
                habilitarCampos(true);
                txtNIB.setEnabled(false); // Não permitir alterar o NIB
                btnSalvar.setEnabled(true);
                btnCancelar.setEnabled(true);
                btnNovo.setEnabled(false);
            }
        }
    }

    private void handleNovo(ActionEvent e) {
        limparCampos();
        habilitarCampos(true);
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        editando = false;
    }

    private void handleSalvar(ActionEvent e) {
        try {
            // Validação dos campos
            String nib = txtNIB.getText().trim();
            String nomeCliente = txtNomeCliente.getText().trim();
            String moeda = (String) cmbMoeda.getSelectedItem();
            double saldoInicial = Double.parseDouble(txtSaldoInicial.getText().trim());

            if (nib.isEmpty() || nomeCliente.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cmbAgencia.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma agência.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Agencia agenciaSelecionada = (Agencia) cmbAgencia.getSelectedItem();

            if (editando) {
                // Atualizando conta existente
                Conta contaAtualizada = new Conta(nib, nomeCliente, moeda, saldoInicial, agenciaSelecionada.getCodigo());
                if (gerenciador.atualizarConta(contaAtualizada)) {
                    JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar a conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Criando nova conta
                Conta novaConta = new Conta(nib, nomeCliente, moeda, saldoInicial, agenciaSelecionada.getCodigo());
                if (gerenciador.adicionarConta(novaConta)) {
                    JOptionPane.showMessageDialog(this, "Conta criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao criar conta. O NIB já pode estar em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Recarregar dados e resetar formulário
            carregarDados();
            limparCampos();
            habilitarCampos(false);
            btnSalvar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnNovo.setEnabled(true);
            editando = false;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido para o saldo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelar(ActionEvent e) {
        limparCampos();
        habilitarCampos(false);
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        editando = false;
        tblContas.clearSelection();
    }
}