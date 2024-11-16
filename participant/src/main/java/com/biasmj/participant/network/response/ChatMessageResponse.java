package com.biasmj.participant.network.response;

public class ChatMessageResponse {
    private final String chatName;
    private final String participantID;
    private final String message;
    private final String regDate;

    public ChatMessageResponse(String message) {
        String[] value = message.split(",");
        this.chatName = value[0];
        this.participantID = value[1];
        this.message = value[2];
        this.regDate = value[3];
    }

    public String formatMessage() {
        return String.format("[%s] %s", regDate, message);
    }
}