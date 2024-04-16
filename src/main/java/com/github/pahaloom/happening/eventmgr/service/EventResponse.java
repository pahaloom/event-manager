package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class EventResponse {

    private UUID id;

    private String name;

    private ZonedDateTime time;

    private String place;

    private int size;
}
