package com.github.pahaloom.happening.eventmgr.model.dao;

import com.github.pahaloom.happening.eventmgr.model.en.EventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventRepository extends CrudRepository<EventEntity, UUID> {

}
