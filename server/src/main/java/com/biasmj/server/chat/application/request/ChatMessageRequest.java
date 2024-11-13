package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.domain.type.ChatType;

import java.time.LocalDateTime;

public class ChatMessageRequest {
    private final ChatType type;
    private final String chatName;
    private final String participantID;
    private final String message;
    private final LocalDateTime regDate;

    public ChatMessageRequest(String message) {
        String[] value = message.split(",");
        this.type = ChatType.valueOf(value[0]);
        this.chatName = value[1];
        this.participantID = value[2];
        this.message = value[3];
        this.regDate = LocalDateTime.now();
    }

    public ChatType getType() {
        return type;
    }

    public String getChatName() {
        return chatName;
    }
    public String getParticipantID() {
        return participantID;
    }
    public String getMessage() {
        return message;
    }
}