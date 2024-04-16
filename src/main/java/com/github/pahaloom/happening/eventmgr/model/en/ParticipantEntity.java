package com.github.pahaloom.happening.eventmgr.model.en;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
@ToString
public abstract class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private PaymentMethodEntity paymentMethod;

    @ManyToMany
    private Set<EventEntity> events;


    public abstract String getName();

    public abstract String getCode();

    public abstract String getExtraInfo();

    public abstract int getCount();
}
