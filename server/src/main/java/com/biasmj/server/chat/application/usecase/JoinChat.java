package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface JoinChat {
    Chat execute(JoinChatRequest request);

    class JoinChatImpl implements JoinChat {
        private final ChatDao chatDao;
        private final ParticipantDao participantDao;
        private static final Logger logger = Logger.getLogger(JoinChat.class.getName());

        public JoinChatImpl() {
            this.chatDao = new ChatDao.ChatDaoImpl();
            this.participantDao = new ParticipantDao.ParticipantDaoImpl();
        }
        public JoinChatImpl(ChatDao chatDao, ParticipantDao participantDao) {
            this.chatDao = chatDao;
            this.participantDao = participantDao;
        }

        @Override
        public Chat execute(JoinChatRequest request) {
            logger.log(Level.INFO, String.format("Participant(%s) is joining chat(%s)", request.participantID, request.chatName));

            Chat chat = chatDao.findChat(request.chatName);
            if (chat.isFull()) {
                logger.log(Level.SEVERE,  "The chat room is currently full.");
                throw new IllegalArgumentException( "The chat room is currently full.");
            }

            Participant participant = participantDao.findParticipant(request.participantID);
            chatDao.updateChat(chat.addParticipant(participant));
            return chat;
        }
    }

    record JoinChatRequest(String chatName, String participantID) {}
}