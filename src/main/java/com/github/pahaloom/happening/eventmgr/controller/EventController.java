package com.github.pahaloom.happening.eventmgr.controller;

import com.github.pahaloom.happening.eventmgr.service.EventRequest;
import com.github.pahaloom.happening.eventmgr.service.EventResponse;
import com.github.pahaloom.happening.eventmgr.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public List<EventResponse> getEvents() {
        return eventService.getEvents();
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
