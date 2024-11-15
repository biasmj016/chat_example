package com.biasmj.server.participant.application.usecase;

import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface LoginParticipant {
    Participant execute(LoginParticipantRequest request);

    class LoginParticipantImpl implements LoginParticipant {
        private final ParticipantDao participantDao;
        private static final Logger logger = Logger.getLogger(LoginParticipant.class.getName());

        public LoginParticipantImpl(ParticipantDao participantDao) {
            this.participantDao = participantDao;
        }

        @Override
        public Participant execute(LoginParticipantRequest request) {
            logger.log(Level.INFO, String.format("%s join chat", request.participantID));

            if (participantDao.isExist(request.participantID)) {
                logger.log(Level.SEVERE, "The participant is already exist.");
                throw new IllegalArgumentException("The participant is already exist.");
            }
            Participant participant = new Participant(request.socket, request.participantID, false);
            participantDao.addParticipant(participant);
            return participant;
        }
    }

    record LoginParticipantRequest(Socket socket, String participantID) {}
}