package com.biasmj.participant.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ParticipantChatIntroGUI extends JFrame implements ActionListener {
    private static final String ERROR_ID_REQUIRED = "ID is required.";
    private static final String ERROR_PORT_REQUIRED = "Port number is required.";
    private static final String ERROR_INVALID_PORT = "Invalid port number.";

    private final JPanel connectPanel = new JPanel(new GridBagLayout());
    private final JTextField participantIDText = new JTextField(20);
    private final JTextField portText = new JTextField(20);
    private final JLabel participantIDLabel = new JLabel("ID : ");
    private final JLabel portLabel = new JLabel("Port : ");
    private final JButton loginButton = new JButton("Connect");

    public ParticipantChatIntroGUI() {
        setupFrame();
        setupComponents();
        setupListeners();
    }

    private void setupFrame() {
        setTitle("Participant Intro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 300);
        setResizable(false);
        setVisible(true);
    }

    private void setupComponents() {
        connectPanel.setBackground(Color.decode("#d8e8f7"));
        Font font = new Font("Arial", Font.BOLD, 14);
        setComponentProperties(participantIDLabel, font, Color.decode("#244667"));
        setComponentProperties(portLabel, font, Color.decode("#244667"));
        setComponentProperties(loginButton, font, Color.decode("#244667"), Color.decode("#a1bfdd"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponentToPanel(participantIDLabel, participantIDText, gbc, 0);
        addComponentToPanel(portLabel, portText, gbc, 1);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton.setPreferredSize(new Dimension(260, 40));
        loginButton.addActionListener(this);
        connectPanel.add(loginButton, gbc);

        add(connectPanel);
    }

    private void setupListeners() {
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) connect(null);
            }
        };
        participantIDText.addKeyListener(enterKeyListener);
        portText.addKeyListener(enterKeyListener);
    }

    private void setComponentProperties(JComponent component, Font font, Color foreground) {
        component.setFont(font);
        component.setForeground(foreground);
    }

    private void setComponentProperties(JButton button, Font font, Color foreground, Color background) {
        button.setFont(font);
        button.setForeground(foreground);
        button.setBackground(background);
    }

    private void addComponentToPanel(JLabel label, JTextField textField, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        connectPanel.add(label, gbc);

        gbc.gridx = 1;
        connectPanel.add(textField, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) connect(e);
    }

    private void connect(ActionEvent e) {
        try {
            String id = participantIDText.getText().trim();
            String port = portText.getText().trim();
            if (id.isBlank()) throw new IllegalArgumentException(ERROR_ID_REQUIRED);
            if (port.isBlank()) throw new IllegalArgumentException(ERROR_PORT_REQUIRED);

            SwingUtilities.invokeLater(() -> new ParticipantChatGUI(id, parsePort(port)));
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