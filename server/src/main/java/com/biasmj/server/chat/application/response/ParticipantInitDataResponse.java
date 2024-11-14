package com.biasmj.server.chat.application.response;

import com.biasmj.server.chat.domain.MessageType;
import com.biasmj.server.chat.domain.type.RequestType;
import com.biasmj.server.participant.domain.Participant;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantInitDataResponse extends MessageType {
    private final List<Participant> participantList;

    public ParticipantInitDataResponse(List<Participant> participantList) {
        super(RequestType.JOIN);
        this.participantList = participantList;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    @Override
    public String toString() {
        String str = super.getTypeName()+ ":";
        str += participantList.stream()
                .map(Participant::participantID)
                .collect(Collectors.joining(","));
        return str;
    }
}