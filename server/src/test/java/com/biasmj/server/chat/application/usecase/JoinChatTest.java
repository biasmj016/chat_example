package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.infrastructure.ParticipantDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JoinChatTest {
    private JoinChat joinChat;
    private ChatDao chatDao;
    private ParticipantDao participantDao;

    @BeforeEach
    void setUp() {
        chatDao = mock(ChatDao.class);
        participantDao = mock(ParticipantDao.class);
        joinChat = new JoinChat.JoinChatImpl(chatDao, participantDao);
    }

    @Test
    void testExecute() {
        Participant manager = new Participant(null, "managerID", true);
        Chat chat = new Chat("chatName", "managerID", manager, 10);
        Participant participant = new Participant(null, "participantID", false);
        when(chatDao.findChat(anyString())).thenReturn(chat);
        when(participantDao.findParticipant(anyString())).thenReturn(participant);

        JoinChat.JoinChatRequest request = new JoinChat.JoinChatRequest("chatName", "participantID");
        Chat result = joinChat.execute(request);

        assertNotNull(result);
        verify(chatDao, times(1)).updateChat(any(Chat.class));
    }
}