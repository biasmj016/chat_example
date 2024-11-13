package com.biasmj.server.participant.domain;

import java.net.Socket;
import java.time.LocalDateTime;

public record Participant(
        Socket socket,
        String id,
        Boolean isManager,
        LocalDateTime loginDate
) {
    public Participant(Socket socket, String id, Boolean isManager) {
        this(socket, id, isManager, LocalDateTime.now());
    }

    public String formatJoinMessage() {
        return id + " joined the chat.";
    }

    public String formatLeaveMessage() {
        return id + " left the chat.";
    }
}