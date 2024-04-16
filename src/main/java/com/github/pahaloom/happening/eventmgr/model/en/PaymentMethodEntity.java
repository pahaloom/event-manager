package com.github.pahaloom.happening.eventmgr.model.en;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
public class PaymentMethodEntity {

    @Id
    private String code;

    private String name;
}
