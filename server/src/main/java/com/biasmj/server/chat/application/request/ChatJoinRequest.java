package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.JoinChat.JoinChatRequest;

public class ChatJoinRequest {
    private final String chatName;
    private final String participantID;


    public ChatJoinRequest(String message) {
        this.chatName = message.split(",")[0];
        this.participantID = message.split(",")[1];
    }

    public JoinChatRequest toUsecase() {
        return new JoinChatRequest(chatName, participantID);
    }

}