package com.github.pahaloom.happening.eventmgr.service;

import com.github.pahaloom.happening.eventmgr.model.dao.PaymentMethodRepository;
import com.github.pahaloom.happening.eventmgr.model.en.PaymentMethodEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentType> getPaymentTypes() {
        var paymentTypes = new ArrayList<PaymentType>();
        paymentMethodRepository.findAll().forEach(pm -> paymentTypes.add(
                new PaymentType()
                        .setCode(pm.getCode())
                        .setName(pm.getName())
        ));
        paymentTypes.sort(Comparator.comparing(PaymentType::getCode));
        return paymentTypes;
    }

    @Transactional
    @Override
    public void addPaymentType(PaymentType paymentType) {
        paymentMethodRepository.save(new PaymentMethodEntity()
                .setCode(paymentType.getCode())
                .setName(paymentType.getName()));
    }

    @Override
    public void removePaymentType(String code) {
        var pme = paymentMethodRepository.findById(code);
        pme.ifPresent(paymentMethodEntity -> paymentMethodRepository.delete(paymentMethodEntity));
    }
}
