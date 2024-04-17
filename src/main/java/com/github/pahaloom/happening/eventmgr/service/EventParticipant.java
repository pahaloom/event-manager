package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class EventParticipant {

    private ParticipantType type;

    private String name;

    private String code;
}
