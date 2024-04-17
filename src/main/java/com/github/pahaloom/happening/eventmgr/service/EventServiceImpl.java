package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.EventRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantJuridicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantPhysicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ParticipantPhysicalRepository participantPhysicalRepository;

    @Autowired
    ParticipantJuridicalRepository participantJuridicalRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

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
                .sorted((e1, e2) -> Math.toIntExact(e1.getTime().compareTo(e2.getTime())))
                .toList();
    }

    @Transactional
    @Override
    public EventResponse getEvent(UUID eventId) {
        var eventResponse = eventRepository.findById(eventId)
                .map(EventServiceImpl::mapEventEntityToResponse);
        if (eventResponse.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return eventResponse.get();
    }

    @Transactional
    @Override
    public UUID createEvent(EventCreationRequest event) {
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
    public boolean addParticipant(UUID eventId, ParticipantType participantType, UUID participantId) {
        var eventEntity = getEventEntity(eventId);
        var retVal = false;
        switch (participantType) {
            case PHYSICAL -> {
                var p = participantPhysicalRepository.findById(participantId);
                if (p.isEmpty()) {
                    throw new IllegalArgumentException("Participant not found: " + participantId);
                }
                retVal = eventEntity.getParticipants().add(p.get());
            }
            case LEGAL -> {
                var p = participantJuridicalRepository.findById(participantId);
                if (p.isEmpty()) {
                    throw new IllegalArgumentException("Participant not found: " + participantId);
                }
                retVal = eventEntity.getParticipants().add(p.get());
            }
        }
        eventRepository.save(eventEntity);
        return retVal;
    }

    @Transactional
    @Override
    public UUID addParticipant(UUID eventId, NewParticipantRequest participant) {
        var eventEntity = getEventEntity(eventId);
        var method = getPaymentMethodEntity(participant.getPaymentTYpe());
        switch (participant.getType()) {
            case PHYSICAL -> {
                var p = new ParticipantPhysicalEntity()
                        .setFirstName(participant.getFirstName())
                        .setLastName(participant.getLastName())
                        .setPersonalCode(participant.getCode())
                        .setInfo(participant.getInfo());
                p.setPaymentMethod(method);
                participantPhysicalRepository.save(p);
                eventEntity.getParticipants().add(p);
                return p.getId();
            }
            case LEGAL -> {
                var p = new ParticipantJuridicalEntity()
                        .setJuridicalName(participant.getName())
                        .setRegCode(participant.getCode())
                        .setParticipants(participant.getCount())
                        .setInfo(participant.getInfo());
                p.setPaymentMethod(method);
                participantJuridicalRepository.save(p);
                eventEntity.getParticipants().add(p);
                return p.getId();
            }
        }
        throw new IllegalArgumentException("Unknown participant type: " + participant.getType());
    }

    private static EventResponse mapEventEntityToResponse(EventEntity ee) {
        return new EventResponse()
                .setId(ee.getId())
                .setName(ee.getName())
                .setTime(ee.getTime())
                .setPlace(ee.getPlace())
                .setSize(ee.getParticipants().stream()
                        .mapToInt(ParticipantEntity::getCount)
                        .sum());
    }

    private PaymentMethodEntity getPaymentMethodEntity(String paymentType) {
        var method = paymentMethodRepository.findById(paymentType);
        if (method.isEmpty()) {
            throw new IllegalArgumentException("Payment method not found: " + paymentType);
        }
        return method.get();
    }

    private EventEntity getEventEntity(UUID eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return event.get();
    }
}
