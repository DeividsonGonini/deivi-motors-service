package com.deivimotors.config;

import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.ports.out.PaymentRepositoryOutputPort;
import com.deivimotors.application.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public PaymentService paymentService(
            PaymentRepositoryOutputPort repository,
            SaleServiceInputPort saleService
    ){
        return new PaymentService(repository, saleService);
    }

}
