package com.biasmj.manager.ui;

import com.biasmj.manager.domain.ChatObserver;
import com.biasmj.manager.service.ManageChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;

public class ManagerChatGUI extends JFrame implements ChatObserver {
    private static final String INITIAL_MESSAGE = "Admin joined the chat.";
    private static final String ADMIN_ID = "Admin";
    private static final String MESSAGE_FORMAT = "%s : %s";

    private final JTextField chatField = new JTextField(45);
    private final TextArea chatArea = new TextArea(20, 50);
    private final TextArea participantsArea = new TextArea(10, 20);
    private final ManageChat manageChat;

    public ManagerChatGUI(int port) {
        this.manageChat = new ManageChat.ManageChatService();
        initializeChatServer(port);
        setupFrame();
        setupComponents();
        broadcastInitialMessage();
    }

    private void initializeChatServer(int port) {
        try {
            String localIpAddress = InetAddress.getLocalHost().getHostAddress();
            manageChat.startServer(port, localIpAddress);
            manageChat.addObserver(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error starting server: " + e.getMessage(),
                    "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }

    private void setupFrame() {
        setTitle("Admin Chat");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void setupComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#a1bfdd"));

        participantsArea.setEditable(false);
        chatArea.setEditable(false);

        mainPanel.add(createLabeledPanel("Participants", participantsArea), BorderLayout.NORTH);
        mainPanel.add(createLabeledPanel("Chat", chatArea), BorderLayout.CENTER);
        mainPanel.add(createInputPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createLabeledPanel(String labelText, Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(labelText), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.add(chatField);
        chatField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
            }
        });
        return inputPanel;
    }

    private void broadcastInitialMessage() {
        update(INITIAL_MESSAGE);
        chatField.setText(null);
    }

    private void sendMessage() {
        String message = chatField.getText().trim();

        if (message.isEmpty()) return;

        String formattedMessage = String.format(MESSAGE_FORMAT, ADMIN_ID, message);
        manageChat.broadcast(formattedMessage);
        chatField.setText(null);
    }
}
