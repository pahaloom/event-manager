package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class NewParticipantRequest {

    private ParticipantType type;

    private String name;

    private String firstName;

    private String lastName;

    private String code;

    private String paymentTYpe;

    private String info;
}
