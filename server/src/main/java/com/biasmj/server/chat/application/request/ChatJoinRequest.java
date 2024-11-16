package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.JoinChat.JoinChatRequest;

import java.net.Socket;

public class ChatJoinRequest {
    private final String chatName;
    private final String participantID;


    public ChatJoinRequest(String message) {
        this.chatName = message.split(",")[0];
        this.participantID = message.split(",")[1];
    }

    public JoinChatRequest toUsecase(Socket socket) {
        return new JoinChatRequest(socket, chatName, participantID);
    }

    public String getChatName() {
        return chatName;
    }

    public String getParticipantID() {
        return participantID;
    }
}