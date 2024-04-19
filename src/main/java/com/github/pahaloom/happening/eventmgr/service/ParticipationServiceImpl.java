package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.EventRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantJuridicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.ParticipantPhysicalRepository;
import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Transactional
    @Override
    public List<EventParticipant> getParticipants(UUID eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        return event.get().getParticipants().stream()
                .map(p -> new EventParticipant()
                        .setId(p.getId())
                        .setName(p.getName())
                        .setCode(p.getCode())
                        .setType(p.getType()))
                .sorted(Comparator.comparing(EventParticipant::getType)
                        .thenComparing(EventParticipant::getName)
                        .thenComparing(EventParticipant::getCode))
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
        return switch (participant.getType()) {
            case PHYSICAL -> {
                var p = new ParticipantPhysicalEntity()
                        .setFirstName(participant.getFirstName())
                        .setLastName(participant.getLastName())
                        .setPersonalCode(participant.getCode())
                        .setInfo(participant.getInfo());
                p.setPaymentMethod(method);
                participantPhysicalRepository.save(p);
                eventEntity.getParticipants().add(p);
                yield p.getId();
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
                yield p.getId();
            }
        };
    }

    @Override
    public ParticipantDetails getParticipant(ParticipantType type, UUID participantId) {
        return switch (type) {
            case PHYSICAL -> {
                var participantPhysical = participantPhysicalRepository.findById(participantId);
                if (participantPhysical.isEmpty()) {
                    throw new IllegalArgumentException("Physical participant not found: " + participantId);
                }
                var p = participantPhysical.get();
                yield new ParticipantDetails()
                        .setId(p.getId())
                        .setFirstName(p.getFirstName())
                        .setLastName(p.getLastName())
                        .setName(p.getName())
                        .setCode(p.getPersonalCode())
                        .setPaymentType(p.getPaymentMethod() != null ? p.getPaymentMethod().getCode() : null)
                        .setInfo(p.getInfo());
            }
            case LEGAL -> {
                var participantJuridical = participantJuridicalRepository.findById(participantId);
                if (participantJuridical.isEmpty()) {
                    throw new IllegalArgumentException("Juridical participant not found: " + participantId);
                }
                var p = participantJuridical.get();
                yield  new ParticipantDetails()
                        .setId(p.getId())
                        .setLegalName(p.getJuridicalName())
                        .setName(p.getName())
                        .setCode(p.getRegCode())
                        .setPaymentType(p.getPaymentMethod() != null ? p.getPaymentMethod().getCode() : null)
                        .setInfo(p.getInfo());
            }
        };
    }

    @Transactional
    @Override
    public boolean removeParticipant(UUID eventId, ParticipantType type, UUID participantId) {
        Optional<EventEntity> byId = eventRepository.findById(eventId);
        if (byId.isEmpty()) {
            return false;
        }
        var event = byId.get();
        boolean retVal = event.getParticipants().removeIf(p -> p.getType() == type && p.getId().equals(participantId));
        if (retVal) {
            eventRepository.save(event);
        }
        return retVal;
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
