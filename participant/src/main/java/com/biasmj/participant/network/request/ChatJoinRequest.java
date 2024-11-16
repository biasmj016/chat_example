package com.biasmj.participant.network.request;

import com.biasmj.participant.domain.MessageType;
import com.biasmj.participant.domain.type.RequestType;

public class ChatJoinRequest extends MessageType {
    private final String chatName;
    private final String participantId;

    public ChatJoinRequest(String chatName, String participantId) {
        super(RequestType.JOIN);
        this.chatName = chatName;
        this.participantId = participantId;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ "#" + chatName + "," + participantId;
    }
}