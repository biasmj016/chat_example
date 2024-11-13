package com.biasmj.server.participant.infrastructure;

import com.biasmj.server.participant.domain.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public interface ParticipantDao {
    Participant addParticipant(Participant participant);
   Participant findParticipant(String participantId);
   boolean isExist(String participantId);

    class ParticipantDaoImpl implements ParticipantDao {
        private final List<Participant> participants = new ArrayList<>();

        @Override
        public Participant addParticipant(Participant participant) {
            participants.add(participant);
            return participant;
        }

        @Override
        public Participant findParticipant(String participantId) {
            return participants.stream()
                    .filter(participant -> participant.id().equals(participantId))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Participant not found"));
        }

        @Override
        public boolean isExist(String participantId) {
            return participants.stream()
                    .anyMatch(participant -> participant.id().equals(participantId));
        }
    }
}