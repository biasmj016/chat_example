package com.biasmj.manager.network.request;

import com.biasmj.manager.domain.MessageType;
import com.biasmj.manager.domain.type.RequestType;

public class ChatMessageRequest extends MessageType {
    private final String chatName;
    private final String participantID;
    private final String message;

    public ChatMessageRequest(String chatName, String participantID, String message) {
        super(RequestType.MESSAGE);
        this.chatName = chatName;
        this.participantID = participantID;
        this.message = message;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ "#" + chatName + "," + participantID+ "," + message;
    }
}