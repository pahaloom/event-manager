package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.EventRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantJuridicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantPhysicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.EventEntity;
import com.github.pahaloom.happening.eventmgr.model.en.ParticipantJuridicalEntity;
import com.github.pahaloom.happening.eventmgr.model.en.ParticipantPhysicalEntity;
import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ParticipationServiceImpl implements ParticipantService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantPhysicalRepository participantPhysicalRepository;

    @Autowired
    private ParticipantJuridicalRepository participantJuridicalRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;


    @Override
    public List<EventParticipant> getParticipants(UUID eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return event.get().getParticipants().stream()
                .map(p -> new EventParticipant()
                        .setName(p.getName())
                        .setCode(p.getCode())
                        .setType(p.getType()))
                .sorted(Comparator.comparing(EventParticipant::getType)
                        .thenComparing(EventParticipant::getCode)
                        .thenComparing(EventParticipant::getName))
                .toList();
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
        var method = getPaymentMethodEntity(participant.getPaymentType());
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

    private EventEntity getEventEntity(UUID eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return event.get();
    }

    private PaymentMethodEntity getPaymentMethodEntity(String paymentType) {
        var method = paymentMethodRepository.findById(paymentType);
        if (method.isEmpty()) {
            throw new IllegalArgumentException("Payment method not found: " + paymentType);
        }
        return method.get();
    }
}
