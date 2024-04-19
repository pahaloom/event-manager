package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class ParticipantDetails {

    private UUID id;

    private ParticipantType type;

    private String firstName;

    private String lastName;

    private String legalName;

    private String name;

    private String code;

    private String paymentType;

    private String info;

    private Integer count;
}
