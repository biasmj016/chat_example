package com.biasmj.participant.ui;

import com.biasmj.participant.application.ParticipantChat;
import com.biasmj.participant.network.request.ChatJoinRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ParticipantChatIntroGUI extends JFrame {
    private static final String ERROR_ID_REQUIRED = "ID is required.";
    private static final String ERROR_CHAT_REQUIRED = "Chat Room is required.";

    private final JPanel connectPanel = new JPanel(new GridBagLayout());
    private final JTextField participantIDText = new JTextField(20);
    private final JLabel participantIDLabel = new JLabel("ID : ");
    private final JLabel chatLabel = new JLabel("Chat : ");
    private final JComboBox<String> chatComboBox = new JComboBox<>();
    private final JButton loginButton = new JButton("Connect");

    public ParticipantChatIntroGUI() {
        setupFrame();
        setupComponents();
        setupListeners();
        setChatArea();
    }

    private void setupFrame() {
        setTitle("Participant Intro");
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
        setComponentProperties(chatLabel, font, Color.decode("#244667"));
        setComponentProperties(loginButton, font, Color.decode("#244667"), Color.decode("#a1bfdd"));

        chatComboBox.setFont(font);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponentToPanel(participantIDLabel, participantIDText, gbc, 0);
        addComponentToPanel(chatLabel, chatComboBox, gbc, 1);

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
            String chatName = (String) chatComboBox.getSelectedItem();
            if (id.isBlank()) throw new IllegalArgumentException(ERROR_ID_REQUIRED);
            if (chatName.isBlank()) throw new IllegalArgumentException(ERROR_CHAT_REQUIRED);

            SwingUtilities.invokeLater(() -> new ParticipantChatGUI(id, chatName));

            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setChatArea() {
        ParticipantChat.chatRoomList.forEach(chatComboBox::addItem);
    }
}