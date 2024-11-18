package com.biasmj.server.chat.application.handler.response;

import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;

public class ChatCreateResponse extends MessageType {
    private final String chatName;
    private final String participantID;

    public ChatCreateResponse(String chatName, String participantID) {
        super(RequestType.CREATE);
        this.chatName = chatName;
        this.participantID = participantID;
    }

    public String getChatName() {
        return chatName;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ "#" + chatName+ "," + participantID;
    }
}