package com.github.pahaloom.happening.eventmgr.model.dao;

import com.github.pahaloom.happening.eventmgr.model.en.ParticipantJuridicalEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ParticipantJuridicalRepository extends CrudRepository<ParticipantJuridicalEntity, UUID> {
}
