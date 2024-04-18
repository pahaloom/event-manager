package com.github.pahaloom.happening.eventmgr.service;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventResponse> getEvents();

    EventDetailsResponse getEvent(UUID eventId);

    UUID createEvent(EventRequest event);

    void updateEvent(EventRequest event, UUID eventId);
}
