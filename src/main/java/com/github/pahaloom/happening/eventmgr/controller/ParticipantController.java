package com.github.pahaloom.happening.eventmgr.controller;

import com.github.pahaloom.happening.eventmgr.service.EventParticipant;
import com.github.pahaloom.happening.eventmgr.service.NewParticipantRequest;
import com.github.pahaloom.happening.eventmgr.service.ParticipantService;
import com.github.pahaloom.happening.eventmgr.service.ParticipantType;
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
    public UUID addParticipant(@PathVariable UUID eventId, @RequestBody NewParticipantRequest request) {
        return participantService.addParticipant(eventId, request);
    }

    @PutMapping("event/{eventId}/participants/{type}/{participantId}")
    public boolean addParticipant(@PathVariable UUID eventId, @PathVariable ParticipantType type, @PathVariable UUID participantId) {
        return participantService.addParticipant(eventId, type, participantId);
    }
}
