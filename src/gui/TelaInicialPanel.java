package gui;

import javax.swing.*;
import java.awt.*;

public class TelaInicialPanel extends JPanel {

    public TelaInicialPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Sistema de Gestão de Cliente Bancário", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = getJPanel();

        add(centerPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("© 2025 - Sistema de Gestão Bancária", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        add(footerLabel, BorderLayout.SOUTH);
    }

    private static JPanel getJPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Bem-vindo ao Sistema de Gestão de Cliente Bancário.<br><br>"
                + "Este sistema permite gerenciar contas bancárias, agências, <br>"
                + "e realizar transações como depósitos e levantamentos.<br><br>"
                + "Utilize o menu acima para navegar entre as funcionalidades."
                + "</div></html>");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(welcomeLabel);
        return centerPanel;
    }
}