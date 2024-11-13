package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.JoinChat.JoinChatRequest;

public class ChatJoinRequest {
    private final String chatName;
    private final String participantId;


    public ChatJoinRequest(String message) {
        this.chatName = message.split(",")[0];
        this.participantId = message.split(",")[1];
    }

    public JoinChatRequest toUsecase() {
        return new JoinChatRequest(chatName, participantId);
    }

    public String getParticipantId() {
        return participantId;
    }
}