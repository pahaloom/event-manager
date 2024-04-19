package com.github.pahaloom.happening.eventmgr.controller;

import com.github.pahaloom.happening.eventmgr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("event/{eventId}/participants")
    public List<EventParticipant> getParticipants(@PathVariable UUID eventId) {
        return participantService.getParticipants(eventId);
    }

    @PostMapping("event/{eventId}/participants")
    public UUID addParticipant(@PathVariable UUID eventId, @RequestBody ParticipantRequest request) {
        return participantService.addParticipant(eventId, request);
    }

    @PutMapping("event/{eventId}/participant/{type}/{participantId}")
    public boolean addParticipant(@PathVariable UUID eventId, @PathVariable ParticipantType type, @PathVariable UUID participantId) {
        return participantService.addParticipant(eventId, type, participantId);
    }

    @DeleteMapping("event/{eventId}/participant/{type}/{participantId}")
    public boolean removeParticipant(@PathVariable UUID eventId, @PathVariable ParticipantType type, @PathVariable UUID participantId) {
        return participantService.removeParticipant(eventId, type, participantId);
    }

    @GetMapping("participant/{type}/{participantId}")
    public ParticipantDetails getParticipant(@PathVariable ParticipantType type, @PathVariable UUID participantId) {
        return participantService.getParticipant(type, participantId);
    }

    @PutMapping("participant/{type}/{participantId}")
    public boolean updateParticipant(@PathVariable ParticipantType type, @PathVariable UUID participantId, @RequestBody ParticipantRequest participant) {
        return participantService.updateParticipant(type, participantId, participant);
    }
}
