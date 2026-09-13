package com.deivimotors.config;

import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.service.CreateSaleService;
import com.deivimotors.application.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateSaleConfig {

    @Bean
    public CreateSaleService createSaleService(
            SaleServiceInputPort saleService,
            PaymentServiceInputPort paymentService
    ) {
        return new CreateSaleService(saleService, paymentService);
    }

}
