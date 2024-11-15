package com.biasmj.manager.ui;

import com.biasmj.manager.application.ManagerChat;
import com.biasmj.manager.network.request.ChatCreateRequest;
import com.biasmj.manager.network.request.ChatMessageRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ManagerChatGUI extends JFrame {
    private final String participantID;
    private final JTextField chatField = new JTextField(45);
    private final TextArea chatArea = new TextArea(20, 50);
    private final TextArea participantsArea = new TextArea(10, 50);

    public ManagerChatGUI(String participantID, String chatName, int limits) {
        this.participantID = participantID;
        init(participantID, chatName, limits);
        setupFrame();
        setupComponents();
    }

    private void init(String participantID, String chatName, int limits) {
        ManagerChat.chatGUI = this;
        ManagerChat.sender.sendMessage(new ChatCreateRequest(chatName, participantID, limits));
    }

    private void setupFrame() {
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void updateTitle(String chatName) {
        setTitle(chatName);
    }

    public void setParticipantsArea(List<String> participants) {
        participantsArea.setText(String.join("\n", participants));
    }

    public void setChatArea(List<String> messages) {
        chatArea.setText(String.join("\n", messages));
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

    private void sendMessage() {
        String message = chatField.getText().trim();
        if (message.isEmpty()) return;

        ChatMessageRequest messageRequest = new ChatMessageRequest(getTitle(), participantID, message);
        ManagerChat.sender.sendMessage(messageRequest);
        chatField.setText(null);
    }
}