package com.biasmj.server.chat.application.response;

import com.biasmj.server.chat.domain.MessageType;

import java.util.List;

import static com.biasmj.server.chat.domain.type.RequestType.CONNECT;

public class ChatInitDataResponse extends MessageType {
    private final List<String> chatList;

    public ChatInitDataResponse(List<String> chatList) {
        super(CONNECT);
        this.chatList = chatList;
    }

    public List<String> getChatList() {
        return chatList;
    }

    public String toString() {
        String type = super.getTypeName() + ":";
        String chatNames = String.join(",", chatList);
        return chatNames.isEmpty() ? type + "empty" : type + chatNames;
    }
}