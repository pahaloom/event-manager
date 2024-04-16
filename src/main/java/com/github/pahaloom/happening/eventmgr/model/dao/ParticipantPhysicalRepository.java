package com.github.pahaloom.happening.eventmgr.model.dao;

import com.github.pahaloom.happening.eventmgr.model.en.ParticipantPhysicalEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ParticipantPhysicalRepository extends CrudRepository<ParticipantPhysicalEntity, UUID> {
}
