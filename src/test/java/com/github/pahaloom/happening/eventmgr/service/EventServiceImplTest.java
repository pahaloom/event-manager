package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    void init() {
        paymentMethodRepository.save(new PaymentMethodEntity()
                .setCode("CASH")
                .setName("Cash payments"));
    }

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
    void testAddingPeople_correct_count() {
        var eventId = createEvent();

        var someoneId = service.addParticipant(eventId, new NewParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("First")
                .setLastName("Last")
                .setCode("1234")
                .setPaymentTYpe("CASH")
                .setInfo("Some info"));
        var corpId = service.addParticipant(eventId, new NewParticipantRequest()
                .setType(ParticipantType.LEGAL)
                .setName("Test company")
                .setCount(7)
                .setCode("Z1234")
                .setPaymentTYpe("CASH")
                .setInfo("Some corporate party"));

        Assertions.assertEquals(8, service.getEvent(eventId).getSize());
    }

    private UUID createEvent(String name, ZonedDateTime time, String place, String info) {
        return service.createEvent(new EventCreationRequest()
                .setName(name)
                .setTime(time)
                .setPlace(place)
                .setInfo(info));
    }

    private UUID createEvent() {
        return createEvent("Just testing", ZonedDateTime.now(), "Somewhere", "Event for EventService integration testing");
    }
}
