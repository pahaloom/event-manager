package com.github.pahaloom.happening.eventmgr.model.en;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
public class ParticipantJuridicalEntity extends ParticipantEntity {

    private String juridicalName;

    private String regCode;

    private int participants;

    @Column(length = 5000)
    private String info;

    @Override
    public String getName() {
        return juridicalName;
    }

    @Override
    public String getCode() {
        return regCode;
    }

    @Override
    public String getExtraInfo() {
        return info;
    }

    @Override
    public int getCount() {
        return participants;
    }
}
