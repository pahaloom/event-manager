package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class EventRequest {

    private String name;

    private ZonedDateTime time;

    private String place;

    private String info;
}
