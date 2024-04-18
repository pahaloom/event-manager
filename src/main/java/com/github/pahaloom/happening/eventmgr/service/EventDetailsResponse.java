package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class EventDetailsResponse extends EventResponse {
    private String info;
}
