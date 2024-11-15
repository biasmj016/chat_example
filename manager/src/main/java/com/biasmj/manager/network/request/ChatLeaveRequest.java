package com.biasmj.manager.network.request;

import com.biasmj.manager.domain.MessageType;
import com.biasmj.manager.domain.type.RequestType;

public class ChatLeaveRequest extends MessageType {
    private final String chatName;
    private final String participantID;

    public ChatLeaveRequest(String chatName, String participantID) {
        super(RequestType.LEAVE);
        this.chatName = chatName;
        this.participantID = participantID;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ "#" + chatName + "," + participantID;
    }
}