package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface LeaveChat {
    Chat execute(LeaveChatRequest request);

    class LeaveChatImpl implements LeaveChat {
        private final ChatDao chatDao;
        private final ParticipantDao participantDao;
        private static final Logger logger = Logger.getLogger(LeaveChat.class.getName());

        public LeaveChatImpl() {
            this.chatDao = new ChatDao.ChatDaoImpl();
            this.participantDao = new ParticipantDao.ParticipantDaoImpl();
        }

        public LeaveChatImpl(ChatDao chatDao, ParticipantDao participantDao) {
            this.chatDao = chatDao;
            this.participantDao = participantDao;
        }

        @Override
        public Chat execute(LeaveChatRequest request) {
            logger.log(Level.INFO, String.format("Participant(%s) is leaving chat(%s)", request.participantID, request.chatName));

            Chat chat = chatDao.findChat(request.chatName);
            Participant participant = participantDao.findParticipant(request.participantID);

            if (chat.participantOnly()) {
                logger.log(Level.INFO, String.format("Only one participant left in the chat. Removing chat(%s)", chat.name()));
                chatDao.removeChat(chat);
                return null;
            }

            if (participant.isManager()) {
                chat.clearParticipants();
            } else {
                chat.removeParticipant(participant);
            }

            return chatDao.updateChat(chat);
        }
    }

    record LeaveChatRequest(String chatName, String participantID) {}
}