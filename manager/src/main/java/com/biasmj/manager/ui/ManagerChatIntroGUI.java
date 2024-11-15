package com.biasmj.manager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ManagerChatIntroGUI extends JFrame {
    private static final String ERROR_ID_REQUIRED = "Manager ID is required.";
    private static final String ERROR_NAME_REQUIRED = "Chat Room Name is required.";
    private static final String ERROR_LIMITS_REQUIRED = "Please select the participant limit.";
    private static final String ERROR_VALUE_LENGTH = "Please enter 10 characters or fewer.";

    private final JPanel connectPanel = new JPanel(new GridBagLayout());
    private final JTextField participantIDText = new JTextField(20);
    private final JTextField nameText = new JTextField(20);
    private final JLabel participantIDLabel = new JLabel("Manager ID : ");
    private final JLabel nameLabel = new JLabel("Chat Name : ");
    private final JLabel limitsLabel = new JLabel("Participant Limit : ");
    private final JComboBox<Integer> limitsComboBox = new JComboBox<>();
    private final JButton loginButton = new JButton("Connect");

    public ManagerChatIntroGUI() {
        setupFrame();
        setupComponents();
        setupListeners();
    }

    private void setupFrame() {
        setTitle("Manager Intro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(450, 300);
        setResizable(false);
        setVisible(true);
    }

    private void setupComponents() {
        connectPanel.setBackground(Color.decode("#d8e8f7"));
        Font font = new Font("Arial", Font.BOLD, 14);
        setComponentProperties(participantIDLabel, font, Color.decode("#244667"));
        setComponentProperties(nameLabel, font, Color.decode("#244667"));
        setComponentProperties(limitsLabel, font, Color.decode("#244667"));
        setComponentProperties(loginButton, font, Color.decode("#244667"), Color.decode("#a1bfdd"));

        for (int i = 1; i <= 10; i++) {
            limitsComboBox.addItem(i);
        }
        limitsComboBox.setFont(font);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponentToPanel(participantIDLabel, participantIDText, gbc, 0);
        addComponentToPanel(nameLabel, nameText, gbc, 1);
        addComponentToPanel(limitsLabel, limitsComboBox, gbc, 2);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton.setPreferredSize(new Dimension(260, 40));
        loginButton.addActionListener(this::connect);
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
        nameText.addKeyListener(enterKeyListener);
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

    private void addComponentToPanel(JLabel label, JComponent component, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        connectPanel.add(label, gbc);

        gbc.gridx = 1;
        connectPanel.add(component, gbc);
    }

    private void connect(ActionEvent e) {
        try {
            String id = participantIDText.getText().trim();
            String chatName = nameText.getText().trim();
            Integer limits = (Integer) limitsComboBox.getSelectedItem();
            if (id.isBlank()) throw new IllegalArgumentException(ERROR_ID_REQUIRED);
            if (chatName.isBlank()) throw new IllegalArgumentException(ERROR_NAME_REQUIRED);
            if (limits == null) throw new IllegalArgumentException(ERROR_LIMITS_REQUIRED);
            if (id.length() >= 10) throw new IllegalArgumentException(ERROR_VALUE_LENGTH);
            if (chatName.length() >= 10) throw new IllegalArgumentException(ERROR_VALUE_LENGTH);

            SwingUtilities.invokeLater(() -> new ManagerChatGUI(id, chatName, limits));

            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}