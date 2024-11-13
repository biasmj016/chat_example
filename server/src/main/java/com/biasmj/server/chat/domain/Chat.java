package com.biasmj.server.chat.domain;

import com.biasmj.server.participant.domain.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Chat(
        String name,
        String managerID,
        List<Participant> participants,
        int limits,
        LocalDateTime regDate
) {

    public Chat(String name, String managerID, Participant participant, int limits) {
        this(name, managerID, new ArrayList<>(List.of(participant)), limits, LocalDateTime.now());
    }

    public Chat addParticipant(Participant participant) {
        participants.add(participant);
        return new Chat(name, managerID, participants, limits, regDate);
    }

    public void clearParticipants() {
        participants.clear();
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
    }

    public boolean participantOnly() {
        return participants.size() == 1;
    }

    public boolean isFull() {
        return participants.size() >= limits;
    }
}