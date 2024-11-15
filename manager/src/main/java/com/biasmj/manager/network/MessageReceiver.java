package com.biasmj.manager.network;

import com.biasmj.manager.application.ManagerChat;
import com.biasmj.manager.domain.Participant;
import com.biasmj.manager.domain.type.RequestType;
import com.biasmj.manager.network.response.ChatCreateResponse;
import com.biasmj.manager.network.response.ChatMessageResponse;
import com.biasmj.manager.network.response.ParticipantInitDataResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MessageReceiver extends Thread {

    private static final Logger logger = Logger.getLogger(MessageReceiver.class.getName());
    private final Socket socket;

    public MessageReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String str = reader.readLine();
                if (str == null) {
                    closeSocket();
                    break;
                }
                logger.info(str);
                String[] token = str.split("#");
                RequestType type = RequestType.valueOf(token[0]);
                String message = token[1];

                processReceivedMessage(type, message);

                Thread.sleep(300);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in socket communication", e);
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
            logger.info("Socket closed");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing socket", e);
        }
    }

    private void processReceivedMessage(RequestType type, String message) {
        logger.info(message);

        switch (type) {
            case MESSAGE -> {
                ChatMessageResponse messageResponse = new ChatMessageResponse(message);
                ManagerChat.chatList.add(messageResponse.formatMessage());
                if (ManagerChat.chatGUI != null) ManagerChat.chatGUI.setChatArea(ManagerChat.chatList);
            }
            case CREATE -> {
                ChatCreateResponse chatCreateResponse = new ChatCreateResponse(message);
                ManagerChat.participants = List.of(chatCreateResponse.getChatName());
                if (ManagerChat.chatGUI != null) {
                    ManagerChat.chatGUI.updateTitle(chatCreateResponse.getChatName());
                    ManagerChat.chatGUI.setParticipantsArea(ManagerChat.participants);
                }
            }
            case JOIN, LEAVE -> {
                ParticipantInitDataResponse participantResponse = new ParticipantInitDataResponse(message);
                ManagerChat.participants = participantResponse.getParticipantList().stream()
                        .map(Participant::id)
                        .collect(Collectors.toList());
                if (ManagerChat.chatGUI != null) {
                    ManagerChat.chatGUI.setParticipantsArea(ManagerChat.participants);
                }
            }
        }
    }
}