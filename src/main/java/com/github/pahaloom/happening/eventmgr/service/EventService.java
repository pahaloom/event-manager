package com.github.pahaloom.happening.eventmgr.service;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventResponse> getEvents();

    UUID createEvent(EventCreationRequest event);

    boolean addParticipant(UUID eventId, ParticipantType type, UUID participantId);

    UUID addParticipant(UUID eventId, NewParticipantRequest participant);
}
