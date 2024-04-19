package com.github.pahaloom.happening.eventmgr.service;

import java.util.List;
import java.util.UUID;

public interface ParticipantService {

    List<EventParticipant> getParticipants(UUID eventId);

    boolean addParticipant(UUID eventId, ParticipantType type, UUID participantId);

    UUID addParticipant(UUID eventId, ParticipantRequest participant);

    ParticipantDetails getParticipant(ParticipantType type, UUID participantId);

    boolean removeParticipant(UUID eventId, ParticipantType type, UUID participantId);

    boolean updateParticipant(ParticipantType type, UUID participantId, ParticipantRequest participant);
}
