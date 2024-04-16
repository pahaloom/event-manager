package com.github.pahaloom.happening.eventmgr.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

@SpringBootTest
public class EventServiceImplTest {

    @Autowired
    EventService service;

    @Test
    void testCreatedEvent_fetching() {
        var eventId = service.createEvent(new EventCreationRequest()
                .setName("Just testing")
                .setTime(ZonedDateTime.now())
                .setPlace("Somewhere")
                .setInfo("Event for EventService integration testing"));
        var fetched = service.getEvents();
        Assertions.assertEquals(1, fetched.size());
        var e = fetched.get(0);
        Assertions.assertEquals(eventId, e.getId());
        Assertions.assertEquals("Just testing", e.getName());
        Assertions.assertEquals("Somewhere", e.getPlace());
        Assertions.assertEquals(0, e.getSize());
    }
}
