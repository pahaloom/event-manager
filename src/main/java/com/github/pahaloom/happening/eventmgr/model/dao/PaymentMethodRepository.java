package com.github.pahaloom.happening.eventmgr.model.dao;

import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethodEntity, String> {
}
