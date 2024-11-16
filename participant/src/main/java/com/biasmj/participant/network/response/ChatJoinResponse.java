package com.biasmj.participant.network.response;

public class ChatJoinResponse {
    private final String chatName;
    private final String participantID;

    public ChatJoinResponse(String message) {
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