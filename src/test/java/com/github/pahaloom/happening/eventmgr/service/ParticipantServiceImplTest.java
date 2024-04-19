package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;
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

        var someoneId = service.addParticipant(eventId, new ParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("First")
                .setLastName("Last")
                .setCode("1234")
                .setPaymentType("CASH")
                .setInfo("Some info"));
        var corpId = service.addParticipant(eventId, new ParticipantRequest()
                .setType(ParticipantType.LEGAL)
                .setName("Test company")
                .setCount(7)
                .setCode("Z1234")
                .setPaymentType("CASH")
                .setInfo("Some corporate party"));

        Assertions.assertEquals(8, eventService.getEvent(eventId).getSize());
    }


    @Test
    void deleteEvent_withParticipants() {
        UUID eId = createEvent();
        service.addParticipant(eId, new ParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("First")
                .setLastName("Last")
                .setCode("312423")
                .setPaymentType("CASH")
                .setInfo("Some happy guy"));
        var isDeleted = eventService.removeEvent(eId);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    void removeParticipant() {
        UUID eId = createEvent();
        UUID eId2 = createEvent("Second", ZonedDateTime.now().plusDays(1), "Cafeteria", "Big and fun party!");
        UUID firstId = service.addParticipant(eId, new ParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("First")
                .setLastName("Last")
                .setCode("525323")
                .setPaymentType("CASH")
                .setInfo("First participant"));
        UUID secondId = service.addParticipant(eId, new ParticipantRequest()
                .setType(ParticipantType.PHYSICAL)
                .setFirstName("Second")
                .setLastName("Last")
                .setCode("525324")
                .setPaymentType("CASH")
                .setInfo("Second participant"));

        Assertions.assertFalse(service.removeParticipant(eId2, ParticipantType.PHYSICAL, firstId));
        Assertions.assertTrue(service.removeParticipant(eId, ParticipantType.PHYSICAL, firstId));

        List<EventParticipant> participants = service.getParticipants(eId);
        Assertions.assertEquals(1, participants.size());
        Assertions.assertEquals(secondId, participants.getFirst().getId());

        Assertions.assertTrue(service.removeParticipant(eId, ParticipantType.PHYSICAL, secondId));
        Assertions.assertEquals(0, service.getParticipants(eId).size());
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
