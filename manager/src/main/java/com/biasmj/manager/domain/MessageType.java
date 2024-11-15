package com.biasmj.manager.domain;

import com.biasmj.manager.domain.type.RequestType;

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