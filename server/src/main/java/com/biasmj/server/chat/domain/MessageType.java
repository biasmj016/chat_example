package com.biasmj.server.chat.domain;

import com.biasmj.server.chat.domain.type.RequestType;

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