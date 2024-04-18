package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class EventParticipant {

    private UUID id;

    private ParticipantType type;

    private String name;

    private String code;
}
