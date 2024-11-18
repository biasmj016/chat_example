package com.biasmj.server.chat.application.usecase;

import com.biasmj.server.chat.domain.Chat;
import com.biasmj.server.chat.infrastructure.ChatDao;
import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface CreateChat {
    Chat execute(CreateChatRequest request);

    class CreateChatImpl implements CreateChat {
        private final ChatDao chatDao;
        private final ParticipantDao participantDao;

        public CreateChatImpl(ChatDao chatDao, ParticipantDao participantDao) {
            this.chatDao = chatDao;
            this.participantDao = participantDao;
        }

        private static final Logger logger = Logger.getLogger(CreateChat.class.getName());

        @Override
        public Chat execute(CreateChatRequest request) {
            logger.log(Level.INFO, String.format("Manager(%s) created chat(%s) with a limit of %d participants.", request.managerID, request.name, request.limits));

            if (participantDao.isExist(request.managerID)) {
                logger.log(Level.SEVERE, "Manager ID already exists.");
                throw new IllegalArgumentException("Manager ID already exists.");
            }

            Participant participant = new Participant(request.socket, request.managerID, true);
            participantDao.addParticipant(participant);

            Chat chat = new Chat(request.name, request.managerID, participant, request.limits);
            chatDao.addChat(chat);

            return chat;
        }
    }

    record CreateChatRequest(Socket socket, String name, String managerID, int limits) {
        public CreateChatRequest(Socket socket, String message) {
            this(socket, message.split(",")[0], message.split(",")[1], Integer.parseInt(message.split(",")[2]));
        }
    }
}