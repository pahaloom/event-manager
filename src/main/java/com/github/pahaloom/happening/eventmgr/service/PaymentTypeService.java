package com.github.pahaloom.happening.eventmgr.service;

import java.util.List;

public interface PaymentTypeService {

    List<PaymentType> getPaymentTypes();

    void addPaymentType(PaymentType paymentType);

    void removePaymentType(String code);
}
