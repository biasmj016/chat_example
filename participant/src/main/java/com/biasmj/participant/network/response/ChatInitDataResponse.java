package com.biasmj.participant.network.response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatInitDataResponse {
    private final List<String> chatList;

    public ChatInitDataResponse(String message) {
        if (message == null || message.isEmpty()) {
            chatList = List.of();
        } else {
            chatList = Arrays.stream(message.split(","))
                    .collect(Collectors.toList());
        }
    }

    public List<String> getChatList() {return chatList;}
}