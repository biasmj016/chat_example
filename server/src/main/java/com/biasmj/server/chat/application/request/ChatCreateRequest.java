package com.biasmj.server.chat.application.request;

import com.biasmj.server.chat.application.usecase.CreateChat.CreateChatRequest;

import java.net.Socket;

public class ChatCreateRequest {
    private final Socket socket;
    private final String chatName;
    private final String participantId;
    private final int limits;


    public ChatCreateRequest(Socket socket, String message) {
        this.socket = socket;
        this.chatName = message.split(",")[0];
        this.participantId = message.split(",")[1];
        this.limits = Integer.parseInt(message.split(",")[2]);
    }

    public CreateChatRequest toUsecase() {
        return new CreateChatRequest(socket, chatName, participantId, limits);
    }
}