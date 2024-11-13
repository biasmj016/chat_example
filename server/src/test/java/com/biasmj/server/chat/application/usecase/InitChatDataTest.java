package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.chat.infrastructure.ChatDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InitChatDataTest {
    private InitChatData initChatData;
    private ChatDao chatDao;

    @BeforeEach
    void setUp() {
        chatDao = mock(ChatDao.class);
        initChatData = new InitChatData.InitChatDataImpl(chatDao);
    }

    @Test
    void testFinds() {
        Participant manager = new Participant(null, "managerID", true);
        when(chatDao.findAllChats()).thenReturn(List.of(new Chat("chatName", "managerID", manager, 10)));

        List<String> result = initChatData.finds();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("chatName", result.get(0));
    }
}