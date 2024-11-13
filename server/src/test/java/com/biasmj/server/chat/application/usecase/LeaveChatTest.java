package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.infrastructure.ParticipantDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveChatTest {
    private LeaveChat leaveChat;
    private ChatDao chatDao;
    private ParticipantDao participantDao;

    @BeforeEach
    void setUp() {
        chatDao = mock(ChatDao.class);
        participantDao = mock(ParticipantDao.class);
        leaveChat = new LeaveChat.LeaveChatImpl(chatDao, participantDao);
    }

    @Test
    void testExecute_ParticipantLeavesChat() {
        Participant manager = new Participant(null, "managerID", true);
        Participant participant = new Participant(null, "participantID", false);
        Chat chat = new Chat("chatName", "managerID", manager, 10);
        chat.addParticipant(participant);

        when(chatDao.findChat("chatName")).thenReturn(chat);
        when(participantDao.findParticipant("participantID")).thenReturn(participant);
        when(chatDao.updateChat(any(Chat.class))).thenReturn(chat);

        LeaveChat.LeaveChatRequest request = new LeaveChat.LeaveChatRequest("chatName", "participantID");
        Chat result = leaveChat.execute(request);

        assertNotNull(result);
        assertEquals(1, result.participants().size());
        assertEquals("managerID", result.participants().get(0).id());
        verify(chatDao, times(1)).updateChat(any(Chat.class));
    }

    @Test
    void testExecute_ManagerLeavesChat() {
        Participant manager = new Participant(null, "managerID", true);
        Chat chat = new Chat("chatName", "managerID", manager, 10);

        when(chatDao.findChat("chatName")).thenReturn(chat);
        when(participantDao.findParticipant("managerID")).thenReturn(manager);

        LeaveChat.LeaveChatRequest request = new LeaveChat.LeaveChatRequest("chatName", "managerID");
        Chat result = leaveChat.execute(request);

        assertNull(result);
        verify(chatDao, times(1)).removeChat(any(Chat.class));
    }

    @Test
    void testExecute_LastParticipantLeavesChat() {
        Participant manager = new Participant(null, "managerID", true);
        Chat chat = new Chat("chatName", "managerID", manager, 10);

        when(chatDao.findChat("chatName")).thenReturn(chat);
        when(participantDao.findParticipant("managerID")).thenReturn(manager);

        LeaveChat.LeaveChatRequest request = new LeaveChat.LeaveChatRequest("chatName", "managerID");
        Chat result = leaveChat.execute(request);

        assertNull(result);
    }
}