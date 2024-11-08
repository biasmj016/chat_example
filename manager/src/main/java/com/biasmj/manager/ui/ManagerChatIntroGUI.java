package com.biasmj.manager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ManagerChatIntroGUI extends JFrame {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 130;
    private static final String ERROR_PORT_REQUIRED = "Port number is required.";
    private static final String ERROR_INVALID_PORT = "Invalid port number.";

    private final JTextField portText = new JTextField(10);
    private final JButton connectButton = new JButton("Connect");

    public ManagerChatIntroGUI() {
        setupFrame();
        setupComponents();
        setupListeners();
    }

    private void setupFrame() {
        setTitle("Manager Intro");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void setupComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Port : "), gbc);

        gbc.gridx = 1;
        panel.add(portText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(connectButton, gbc);

        add(panel);
    }

    private void setupListeners() {
        connectButton.addActionListener(this::connect);
        portText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) connect(null);
            }
        });
    }

    private void connect(ActionEvent e) {
        try {
            String port = portText.getText().trim();
            if (port.isBlank()) throw new IllegalArgumentException(ERROR_PORT_REQUIRED);

            SwingUtilities.invokeLater(() -> new ManagerChatGUI(parsePort(port)));
            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parsePort(String portText) {
        int port = Integer.parseInt(portText);
        if (port < 0 || port > 65535) throw new IllegalArgumentException(ERROR_INVALID_PORT);
        return port;
    }
}