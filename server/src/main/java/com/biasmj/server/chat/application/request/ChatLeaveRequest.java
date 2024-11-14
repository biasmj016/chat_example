package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.LeaveChat.LeaveChatRequest;

public class ChatLeaveRequest {
    private final String chatName;
    private final String participantID;

    public ChatLeaveRequest(String message) {
        this.chatName = message.split(",")[0];
        this.participantID = message.split(",")[1];
    }

    public String getChatName() {
        return chatName;
    }
    public String getParticipantId() {
        return participantID;
    }
    public LeaveChatRequest toUsecase() {
        return new LeaveChatRequest(chatName, participantID);
    }
}