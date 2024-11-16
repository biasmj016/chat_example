package com.biasmj.participant.ui;

import com.biasmj.participant.network.request.ChatMessageRequest;
import com.biasmj.participant.application.ParticipantChat;
import com.biasmj.participant.network.request.ChatJoinRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ParticipantChatGUI extends JFrame {
    private final String participantID;
    private final String chatName;
    private final JTextField chatField = new JTextField(45);
    private final TextArea chatArea = new TextArea(20, 50);
    private final TextArea participantsArea = new TextArea(10, 50);

    public ParticipantChatGUI(String participantID, String chatName) {
        this.participantID = participantID;
        this.chatName = chatName;
        init();
        setupFrame();
        setupComponents();
    }

    private void init() {
        ParticipantChat.chatGUI = this;
        ParticipantChat.sender.sendMessage(new ChatJoinRequest(chatName, participantID));
    }

    private void setupFrame() {
        updateTitle();
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void updateTitle() {
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
        ParticipantChat.sender.sendMessage(messageRequest);
        chatField.setText(null);
    }
}