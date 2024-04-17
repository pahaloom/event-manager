package com.github.pahaloom.happening.eventmgr.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PaymentType {

    private String code;

    private String name;
}
