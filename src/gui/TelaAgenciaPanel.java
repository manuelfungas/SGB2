
package gui;

import model.Agencia;
import model.GerenciadorDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaAgenciaPanel extends JPanel {
    private GerenciadorDados gerenciador;

    // Componentes da interface
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtLocalizacao;
    private JTextField txtTelefone;
    private JTable tblAgencias;
    private DefaultTableModel tableModel;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnCancelar;

    // Controle de estado
    private boolean editando = false;

    public TelaAgenciaPanel(GerenciadorDados gerenciador) {
        this.gerenciador = gerenciador;
        setupUI();
        carregarDados();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Agência"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Código
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Código:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCodigo = new JTextField(10);
        formPanel.add(txtCodigo, gbc);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField(30);
        formPanel.add(txtNome, gbc);

        // Localização
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Localização:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtLocalizacao = new JTextField(30);
        formPanel.add(txtLocalizacao, gbc);

        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(15);
        formPanel.add(txtTelefone, gbc);

        // Painel de botões do formulário
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnNovo = new JButton("Nova Agência");
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Agências Cadastradas"));

        // Configuração da tabela
        String[] columnNames = {"Código", "Nome", "Localização", "Telefone"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta na tabela
            }
        };

        tblAgencias = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblAgencias);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Adicionar os painéis ao layout principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Configurar os eventos dos botões
        btnNovo.addActionListener(this::handleNovo);
        btnSalvar.addActionListener(this::handleSalvar);
        btnCancelar.addActionListener(this::handleCancelar);

        tblAgencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblAgencias.getSelectedRow() != -1) {
                carregarAgenciaSelecionada();
            }
        });

        // Estado inicial
        habilitarCampos(false);
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    private void carregarDados() {
        // Carregar tabela de agências
        tableModel.setRowCount(0);
        for (Agencia agencia : gerenciador.getTodasAgencias()) {
            tableModel.addRow(new Object[]{
                    agencia.getCodigo(),
                    agencia.getNome(),
                    agencia.getLocalizacao(),
                    agencia.getTelefone()
            });
        }
    }

    private void habilitarCampos(boolean habilitar) {
        txtCodigo.setEnabled(habilitar);
        txtNome.setEnabled(habilitar);
        txtLocalizacao.setEnabled(habilitar);
        txtTelefone.setEnabled(habilitar);
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtLocalizacao.setText("");
        txtTelefone.setText("");
    }

    private void carregarAgenciaSelecionada() {
        int selectedRow = tblAgencias.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) tableModel.getValueAt(selectedRow, 0);
            Agencia agencia = gerenciador.buscarAgenciaPorCodigo(codigo);

            if (agencia != null) {
                txtCodigo.setText(agencia.getCodigo());
                txtNome.setText(agencia.getNome());
                txtLocalizacao.setText(agencia.getLocalizacao());
                txtTelefone.setText(agencia.getTelefone());

                editando = true;
                habilitarCampos(true);
                txtCodigo.setEnabled(false); // Não permitir alterar o código
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
        // Validação dos campos
        String codigo = txtCodigo.getText().trim();
        String nome = txtNome.getText().trim();
        String localizacao = txtLocalizacao.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (codigo.isEmpty() || nome.isEmpty() || localizacao.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (editando) {
            // Atualizando agência existente
            Agencia agenciaAtualizada = new Agencia(codigo, nome, localizacao, telefone);
            if (gerenciador.atualizarAgencia(agenciaAtualizada)) {
                JOptionPane.showMessageDialog(this, "Agência atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar a agência.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Criando nova agência
            Agencia novaAgencia = new Agencia(codigo, nome, localizacao, telefone);
            if (gerenciador.adicionarAgencia(novaAgencia)) {
                JOptionPane.showMessageDialog(this, "Agência criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar agência. O código já pode estar em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
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
    }

    private void handleCancelar(ActionEvent e) {
        limparCampos();
        habilitarCampos(false);
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        editando = false;
        tblAgencias.clearSelection();
    }
}