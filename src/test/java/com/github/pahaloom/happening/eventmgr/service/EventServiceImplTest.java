package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.UUID;

@SpringBootTest
public class EventServiceImplTest {

    @Autowired
    EventService service;

    @Test
    void testCreatedEvent_fetching() {
        var eventId = createEvent();
        var fetched = service.getEvents();
        Assertions.assertEquals(1, fetched.size());
        var e = fetched.getFirst();
        Assertions.assertEquals(eventId, e.getId());
        Assertions.assertEquals("Just testing", e.getName());
        Assertions.assertEquals("Somewhere", e.getPlace());
        Assertions.assertEquals(0, e.getSize());
    }

    @Test
    void deleteEvent () {
        UUID eId = createEvent();
        var isDeleted = service.removeEvent(eId);
        Assertions.assertTrue(isDeleted);
    }

    private UUID createEvent(String name, ZonedDateTime time, String place, String info) {
        return service.createEvent(new EventRequest()
                .setName(name)
                .setTime(time)
                .setPlace(place)
                .setInfo(info));
    }

    private UUID createEvent() {
        return createEvent("Just testing", ZonedDateTime.now(), "Somewhere", "Event for EventService integration testing");
    }
}
