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
public class ParticipantServiceImplTest {

    @Autowired
    ParticipantService service;

    @Autowired
    EventService eventService;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    void init() {
        paymentMethodRepository.save(new PaymentMethodEntity()
                .setCode("CASH")
                .setName("Cash payments"));
    }

    @Test
    void testAddingPeople_correct_count() {
        var eventId = createEvent();

        var someoneId = service.addParticipant(eventId, new NewParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("First")
                .setLastName("Last")
                .setCode("1234")
                .setPaymentType("CASH")
                .setInfo("Some info"));
        var corpId = service.addParticipant(eventId, new NewParticipantRequest()
                .setType(ParticipantType.LEGAL)
                .setName("Test company")
                .setCount(7)
                .setCode("Z1234")
                .setPaymentType("CASH")
                .setInfo("Some corporate party"));

        Assertions.assertEquals(8, eventService.getEvent(eventId).getSize());
    }

    private UUID createEvent(String name, ZonedDateTime time, String place, String info) {
        return eventService.createEvent(new EventRequest()
                .setName(name)
                .setTime(time)
                .setPlace(place)
                .setInfo(info));
    }

    private UUID createEvent() {
        return createEvent("Just testing", ZonedDateTime.now(), "Somewhere", "Event for ParticipantService integration testing");
    }
}
