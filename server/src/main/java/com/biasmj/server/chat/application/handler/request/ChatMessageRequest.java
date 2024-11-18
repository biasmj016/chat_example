package com.biasmj.server.chat.application.handler.request;

public class ChatMessageRequest {
    private final String chatName;
    private final String participantID;
    private final String message;

    public ChatMessageRequest(String message) {
        String[] value = message.split(",");
        this.chatName = value[0];
        this.participantID = value[1];
        this.message = value[2];
    }

    public String getChatName() {
        return chatName;
    }
    public String getParticipantID() {return participantID;}
    public String getMessage() {return participantID +" : "+message;}
}