package com.biasmj.manager.network.request;

import com.biasmj.manager.domain.MessageType;
import com.biasmj.manager.domain.type.RequestType;

public class ChatCreateRequest extends MessageType {
    private final String chatName;
    private final String participantId;
    private final int limits;

    public ChatCreateRequest(String chatName, String participantId, int limits) {
        super(RequestType.CREATE);
        this.chatName = chatName;
        this.participantId = participantId;
        this.limits = limits;
    }

    @Override
    public String toString() {
        return super.getTypeName()+ "#" + chatName + "," + participantId + "," + limits;
    }
}