package com.biasmj.server.participant.application.usecase;

import com.biasmj.server.participant.domain.Participant;
import com.biasmj.server.participant.infrastructure.ParticipantDao;

public interface FindParticipant {
    Participant find(String participantID);

    class FindParticipantImpl implements FindParticipant {
        private final ParticipantDao participantDao;

        public FindParticipantImpl(ParticipantDao participantDao) {
            this.participantDao = participantDao;
        }

        @Override
        public Participant find(String participantID) {
            return participantDao.findParticipant(participantID);
        }
    }

}