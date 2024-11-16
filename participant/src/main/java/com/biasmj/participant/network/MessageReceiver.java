package com.biasmj.participant.network;

import com.biasmj.participant.application.ParticipantChat;
import com.biasmj.participant.domain.Participant;
import com.biasmj.participant.domain.type.RequestType;
import com.biasmj.participant.network.response.ChatInitDataResponse;
import com.biasmj.participant.network.response.ChatMessageResponse;
import com.biasmj.participant.network.response.ParticipantInitDataResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
                logger.info("Received message: " + str);
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
            case CONNECT -> {
                ChatInitDataResponse messageResponse = new ChatInitDataResponse(message);
                ParticipantChat.chatRoomList= messageResponse.getChatList();
                if (ParticipantChat.introGUI != null) ParticipantChat.introGUI.setChatArea();
            }
            case MESSAGE -> {
                ChatMessageResponse messageResponse = new ChatMessageResponse(message);
                ParticipantChat.chatList.add(messageResponse.formatMessage());
                if (ParticipantChat.chatGUI != null) ParticipantChat.chatGUI.setChatArea(ParticipantChat.chatList);
            }
            case JOIN, LEAVE -> {
                ParticipantInitDataResponse participantResponse = new ParticipantInitDataResponse(message);
                ParticipantChat.participants = participantResponse.getParticipantList().stream()
                        .map(Participant::id)
                        .collect(Collectors.toList());
                if (ParticipantChat.chatGUI != null) {
                    ParticipantChat.chatGUI.setParticipantsArea(ParticipantChat.participants);
                }
            }
        }
    }
}