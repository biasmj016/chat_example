package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.chat.infrastructure.ChatDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindChatTest {
    private FindChat findChat;
    private ChatDao chatDao;

    @BeforeEach
    void setUp() {
        chatDao = mock(ChatDao.class);
        findChat = new FindChat.FindChatImpl(chatDao);
    }

    @Test
    void testFind() {
        Participant manager = new Participant(null, "managerID", true);
        Chat chat = new Chat("chatName", "managerID", manager, 10);
        when(chatDao.findChat(anyString())).thenReturn(chat);

        Chat result = findChat.find("chatName");

        assertNotNull(result);
        assertEquals("chatName", result.name());
    }
}