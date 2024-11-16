package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface JoinChat {
    Chat execute(JoinChatRequest request);

    class JoinChatImpl implements JoinChat {
        private final ChatDao chatDao;
        private final ParticipantDao participantDao;
        private static final Logger logger = Logger.getLogger(JoinChat.class.getName());

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

            if (participantDao.isExist(request.participantID)) {
                logger.log(Level.SEVERE, "The participant is already exist.");
                throw new IllegalArgumentException("The participant is already exist.");
            }

            Participant participant = new Participant(request.socket, request.participantID, false);

            participantDao.addParticipant(participant);
            chatDao.updateChat(chat.addParticipant(participant));
            return chat;
        }
    }

    record JoinChatRequest(Socket socket, String chatName, String participantID) {}
}