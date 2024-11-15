package com.biasmj.manager.network.response;

public class ChatCreateResponse {
    private final String chatName;
    private final String participantID;

    public ChatCreateResponse(String message) {
        String[] value = message.split(",");
        this.chatName = value[0];
        this.participantID = value[1];
    }

    public String getChatName() {
        return chatName;
    }

    public String getParticipantID() {
        return participantID;
    }
}