package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.infrastructure.ParticipantDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateChatTest {
    private CreateChat createChat;
    private ChatDao chatDao;
    private ParticipantDao participantDao;

    @BeforeEach
    void setUp() {
        chatDao = mock(ChatDao.class);
        participantDao = mock(ParticipantDao.class);
        createChat = new CreateChat.CreateChatImpl(chatDao, participantDao);
    }

    @Test
    void testExecute() {
        Socket socket = mock(Socket.class);
        CreateChat.CreateChatRequest request = new CreateChat.CreateChatRequest(socket, "chatName", "managerID", 10);

        when(participantDao.isExist(anyString())).thenReturn(false);
        when(chatDao.addChat(any(Chat.class))).thenReturn(new Chat("chatName", "managerID", new Participant(socket, "managerID", true), 10));

        Chat chat = createChat.execute(request);

        assertNotNull(chat);
        assertEquals("chatName", chat.name());
    }
}