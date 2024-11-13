package com.biasmj.server.chat.application.response;

import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;

public class ChatCreateResponse extends MessageType {
    private final String chatName;

    public ChatCreateResponse(String chatName) {
        super(RequestType.CREATE);
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ ":" + chatName;
    }
}