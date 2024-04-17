package com.github.pahaloom.happening.eventmgr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataRunner implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InitialDataRunner.class);

    @Autowired
    private PaymentTypeService paymentTypeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("=== Adding initial data ===");
        paymentTypeService.addPaymentType(new PaymentType()
                .setCode("TRANSFER")
                .setName("Pangaülekanne"));
        paymentTypeService.addPaymentType(new PaymentType()
                .setCode("CASH")
                .setName("Sularaha"));
        LOG.info("=== Done adding initial data ===");
    }
}
