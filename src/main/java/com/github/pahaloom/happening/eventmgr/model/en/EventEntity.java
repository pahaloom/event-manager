package com.github.pahaloom.happening.eventmgr.model.en;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private ZonedDateTime time;

    private String place;

    @Column(length = 1000)
    private String info;

    @ManyToMany
    @JoinTable(name = "event_participants",
            joinColumns = @JoinColumn(name = "participant_id",
                    foreignKey = @ForeignKey(name = "FK_EVNTPRT_PRTID")),
            inverseJoinColumns = @JoinColumn(name = "event_id",
                    foreignKey = @ForeignKey(name = "FK_EVNTPRT_EVNTID")))
    private Set<ParticipantEntity> participants;
}
