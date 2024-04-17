package com.github.pahaloom.happening.eventmgr.controller;

import com.github.pahaloom.happening.eventmgr.service.PaymentType;
import com.github.pahaloom.happening.eventmgr.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @GetMapping("ptypes")
    public List<PaymentType> getPaymentTypes() {
        return paymentTypeService.getPaymentTypes();
    }

    @PutMapping("ptypes")
    public void addPaymentType(@RequestBody PaymentType paymentType) {
        paymentTypeService.addPaymentType(paymentType);
    }

    @DeleteMapping("ptypes/{code}")
    public void removePaymentType(@PathVariable String code) {
        paymentTypeService.removePaymentType(code);
    }
}
