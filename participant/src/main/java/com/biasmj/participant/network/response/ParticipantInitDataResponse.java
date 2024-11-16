package com.biasmj.participant.network.response;

import com.biasmj.participant.domain.Participant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantInitDataResponse {
    private final List<Participant> participantList;

    public ParticipantInitDataResponse(String message) {
        if (message == null || message.isEmpty()) {
            participantList = List.of();
        } else {
            participantList = Arrays.stream(message.split(","))
                    .map(Participant::new)
                    .collect(Collectors.toList());
        }
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }
}