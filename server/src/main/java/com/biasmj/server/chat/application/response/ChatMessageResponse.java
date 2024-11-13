package com.biasmj.server.chat.application.response;

import com.biasmj.server.chat.domain.type.ChatType;
import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessageResponse extends MessageType {
    private final ChatType type;
    private final String chatName;
    private final String participantID;
    private final String message;
    private final String regDate;

    public ChatMessageResponse(ChatType type, String chatName, String participantID, String message, LocalDateTime regDate) {
        super(RequestType.MESSAGE);
        this.type = type;
        this.chatName = chatName;
        this.participantID = participantID;
        this.message = message;
        this.regDate = convert(regDate);
    }

    public ChatMessageResponse(ChatType type, String chatName, String participantID, String message) {
        super(RequestType.MESSAGE);
        this.type = type;
        this.chatName = chatName;
        this.participantID = participantID;
        this.message = message;
        this.regDate = null;
    }

    public String getParticipantID() {
        return participantID;
    }

    @Override
    public String toString() {
        if(regDate == null) return super.getTypeName()+ ":" + type + "," + chatName + "," + participantID + "," + message;
        return super.getTypeName()+ ":" + type + "," + chatName + "," + participantID + "," + message + "," + regDate;
    }

    private String convert(LocalDateTime regDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        return regDate.format(formatter);
    }
}