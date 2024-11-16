package com.biasmj.participant.domain;

import com.biasmj.participant.domain.type.RequestType;

public abstract class MessageType {
    private final RequestType requestType;

    public MessageType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getTypeName() {
        return requestType.name();
    }
}