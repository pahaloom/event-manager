package com.github.pahaloom.happening.eventmgr.model.en;

import com.github.pahaloom.happening.eventmgr.service.ParticipantType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Accessors(chain = true)
@ToString
public abstract class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PRTCPNT_PYMNTMTHD"))
    private PaymentMethodEntity paymentMethod;

    @ManyToMany(mappedBy = "participants")
    private Set<EventEntity> events;


    public abstract String getName();

    public abstract String getCode();

    public abstract String getExtraInfo();

    public abstract int getCount();

    public abstract ParticipantType getType();
}
