package com.biasmj.server.chat.application.handler.response;

import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessageResponse extends MessageType {
    private final String chatName;
    private final String participantID;
    private final String message;
    private final String regDate;

    public ChatMessageResponse(String chatName, String participantID, String message) {
        super(RequestType.MESSAGE);
        this.chatName = chatName;
        this.participantID = participantID;
        this.message = message;
        this.regDate =  convert(LocalDateTime.now());
    }

    public String getParticipantID() {
        return participantID;
    }

    public String getChatName() {return chatName;}

    @Override
    public String toString() {
        return super.getTypeName()+ "#" +  chatName + "," + participantID + "," + message + "," + regDate;
    }

    private String convert(LocalDateTime regDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        return regDate.format(formatter);
    }
}