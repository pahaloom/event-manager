package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.EventRepository;
import com.github.pahaloom.happening.eventmgr.model.en.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Transactional
    @Override
    public List<EventResponse> getEvents() {
        var it = eventRepository.findAll().iterator();
        var retVal = new ArrayList<EventResponse>();
        while (it.hasNext()) {
            var ee = it.next();
            retVal.add(mapEventEntityToResponse(ee));
        }
        return retVal.stream()
                .sorted(Comparator.comparing(EventResponse::getTime)
                        .thenComparing(EventResponse::getName)
                        .thenComparing(EventResponse::getPlace)
                        .thenComparing(EventResponse::getId))
                .toList();
    }

    @Transactional
    @Override
    public EventDetailsResponse getEvent(UUID eventId) {
        var eventResponse = eventRepository.findById(eventId)
                .map(EventServiceImpl::mapEventEntityToDetailsResponse);
        if (eventResponse.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return (EventDetailsResponse) eventResponse.get();
    }

    @Transactional
    @Override
    public UUID createEvent(EventRequest event) {
        var en = new EventEntity()
                .setName(event.getName())
                .setTime(event.getTime())
                .setPlace(event.getPlace())
                .setInfo(event.getInfo());
        eventRepository.save(en);
        return en.getId();
    }

    @Transactional
    @Override
    public void updateEvent(EventRequest event, UUID eventId) {
        eventRepository.findById(eventId)
                .map(eventEntity -> {
                    eventEntity.setName(event.getName())
                            .setTime(event.getTime())
                            .setPlace(event.getPlace())
                            .setInfo(event.getInfo());
                    return eventRepository.save(eventEntity);
                }).orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
    }

    private static EventResponse mapEventEntityToResponse(EventEntity ee) {
        return new EventResponse()
                .setId(ee.getId())
                .setName(ee.getName())
                .setTime(ee.getTime())
                .setPlace(ee.getPlace())
                .setSize(getParticipantCount(ee));
    }

    private static Object mapEventEntityToDetailsResponse(EventEntity ee) {
        var ed = new EventDetailsResponse();
        ed.setId(ee.getId());
        ed.setName(ee.getName());
        ed.setTime(ee.getTime());
        ed.setPlace(ee.getPlace());
        ed.setInfo(ee.getInfo());
        ed.setSize(getParticipantCount(ee));
        return ed;
    }

    private static int getParticipantCount(EventEntity ee) {
        return ee.getParticipants().stream()
                .mapToInt(ParticipantEntity::getCount)
                .sum();
    }
}
