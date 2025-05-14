package sistema;

import gui.*;
import model.GerenciadorDados;

import javax.swing.*;
import java.awt.*;

public class SistemaBancario {
    private GerenciadorDados gerenciador;
    private JFrame mainFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public SistemaBancario() {
        gerenciador = new GerenciadorDados();
        iniciarUI();
    }

    private void iniciarUI() {
        // Configuração da janela principal
        mainFrame = new JFrame("Sistema de Gestão de Cliente Bancário");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 600);
        mainFrame.setMinimumSize(new Dimension(800, 500));

        // Criação do menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastro = new JMenu("Cadastro");
        JMenuItem itemConta = new JMenuItem("Contas");
        JMenuItem itemAgencia = new JMenuItem("Agências");
        menuCadastro.add(itemConta);
        menuCadastro.add(itemAgencia);

        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemTransacao = new JMenuItem("Transações");
        menuOperacoes.add(itemTransacao);

        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemRelatorioClientes = new JMenuItem("Relatório de Clientes");
        menuRelatorios.add(itemRelatorioClientes);

        menuBar.add(menuCadastro);
        menuBar.add(menuOperacoes);
        menuBar.add(menuRelatorios);

        mainFrame.setJMenuBar(menuBar);

        // Configuração do layout de cards
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Criar painéis para cada funcionalidade
        TelaContaPanel telaContaPanel = new TelaContaPanel(gerenciador);
        TelaAgenciaPanel telaAgenciaPanel = new TelaAgenciaPanel(gerenciador);
        TelaTransacaoPanel telaTransacaoPanel = new TelaTransacaoPanel(gerenciador);
        TelaRelatorioPanel telaRelatorioPanel = new TelaRelatorioPanel(gerenciador);
        TelaInicialPanel telaInicialPanel = new TelaInicialPanel();

        // Adicionar painéis ao card layout
        cardPanel.add(telaInicialPanel, "Inicial");
        cardPanel.add(telaContaPanel, "Contas");
        cardPanel.add(telaAgenciaPanel, "Agencias");
        cardPanel.add(telaTransacaoPanel, "Transacoes");
        cardPanel.add(telaRelatorioPanel, "Relatorios");

        mainFrame.add(cardPanel);

        // Configurar ações dos itens de menu
        itemConta.addActionListener(e -> cardLayout.show(cardPanel, "Contas"));
        itemAgencia.addActionListener(e -> cardLayout.show(cardPanel, "Agencias"));
        itemTransacao.addActionListener(e -> cardLayout.show(cardPanel, "Transacoes"));
        itemRelatorioClientes.addActionListener(e -> cardLayout.show(cardPanel, "Relatorios"));

        // Exibir a tela inicial
        cardLayout.show(cardPanel, "Inicial");

        // Centralizar e exibir a janela
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Definir o look and feel para uma aparência mais moderna
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar o aplicativo no EDT
        SwingUtilities.invokeLater(() -> {
            new SistemaBancario();
        });
    }
}