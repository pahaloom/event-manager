package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ParticipantRequest {

    private ParticipantType type;

    private String name;

    private String firstName;

    private String lastName;

    private int count = 1;

    private String code;

    private String paymentType;

    private String info;
}
