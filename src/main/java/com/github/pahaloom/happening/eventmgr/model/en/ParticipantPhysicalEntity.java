package com.github.pahaloom.happening.eventmgr.model.en;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString
public class ParticipantPhysicalEntity extends ParticipantEntity {

    private String firstName;

    private String lastName;

    private String personalCode;

    @Column(length = 1500)
    private String info;

    @Override
    public String getName() {
        return String.join(" ", Stream.of(firstName, lastName)
                .filter(Objects::nonNull).toList());
    }

    @Override
    public String getCode() {
        return personalCode;
    }

    @Override
    public String getExtraInfo() {
        return info;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
