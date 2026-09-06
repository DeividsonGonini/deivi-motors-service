package com.deivimotors.config;

import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.application.service.SaleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaleConfig {

    @Bean
    public SaleService saleService(
            SaleRepositoryOutputPort repository,
            VehicleServiceInputPort vehicleService
    ) {
        return new SaleService(repository, vehicleService);
    }
}
