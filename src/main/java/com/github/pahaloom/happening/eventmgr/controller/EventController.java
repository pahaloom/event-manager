package com.github.pahaloom.happening.eventmgr.controller;

import com.github.pahaloom.happening.eventmgr.service.EventDetailsResponse;
import com.github.pahaloom.happening.eventmgr.service.EventRequest;
import com.github.pahaloom.happening.eventmgr.service.EventResponse;
import com.github.pahaloom.happening.eventmgr.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public List<EventResponse> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/events/{id}")
    public EventDetailsResponse getEvent(@PathVariable UUID id) {
        return eventService.getEvent(id);
    }

    @PostMapping("/events")
    public UUID createEvent(@RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }

    @PutMapping("/events/{id}")
    public void updateEvent(@RequestBody EventRequest request, @PathVariable UUID id) {
        eventService.updateEvent(request, id);
    }
}
