package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.LeaveChat.LeaveChatRequest;

public class ChatLeaveRequest {
    private final String chatName;
    private final String participantId;

    public ChatLeaveRequest(String message) {
        this.chatName = message.split(",")[0];
        this.participantId = message.split(",")[1];
    }

    public String getChatName() {
        return chatName;
    }
    public String getParticipantId() {
        return participantId;
    }
    public LeaveChatRequest toUsecase() {
        return new LeaveChatRequest(chatName, participantId);
    }
}