package com.biasmj.participant.ui;

import com.biasmj.participant.domain.ChatObserver;
import com.biasmj.participant.handler.ParticipantHandler;
import com.biasmj.participant.service.ManageChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ParticipantChatGUI extends JFrame  implements ChatObserver {
    private static final String INITIAL_MESSAGE = " joined the chat.";
    private final String participantID;
    private static final String MESSAGE_FORMAT = "%s : %s";
    private ManageChat manageChat;
    private final JPanel chatPanel = new JPanel(new BorderLayout());
    private final JLabel participantLabel = new JLabel("Participants");
    private final JTextField chatField = new JTextField(45);
    private final TextArea chatArea = new TextArea(20, 50);
    private final TextArea participantsArea = new TextArea(10, 50);

    public ParticipantChatGUI(String participantID, int port) {
        this.participantID = participantID;
        this.manageChat = new ManageChat.ManageChatService();

        initializeChatServer(port, participantID);
        setupFrame();
        setupComponents();
        broadcastInitialMessage();
    }

    private void initializeChatServer(int port, String participantID) {
        try {
            manageChat.connectToServer(port, participantID);
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
        setTitle(participantID + "'s Chat");
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

    public void updateParticipantList(List<String> nickNames) {
        participantsArea.setText(String.join("\n", nickNames));
    }

    private void broadcastInitialMessage() {
        update(participantID + INITIAL_MESSAGE);
        chatField.setText(null);
    }

    private void sendMessage() {
        String message = chatField.getText().trim();

        if (message.isEmpty()) return;

        String formattedMessage = String.format(MESSAGE_FORMAT, participantID, message);
        manageChat.broadcast(formattedMessage);
        chatField.setText(null);
    }
}