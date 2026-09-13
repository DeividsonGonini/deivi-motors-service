package com.deivimotors.config;

import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.application.service.SaleService;
import com.deivimotors.config.security.AuthenticatedUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaleConfig {

    @Bean
    public SaleService saleService(
            SaleRepositoryOutputPort repository,
            VehicleServiceInputPort vehicleService,
            AuthenticatedUserProvider authenticatedUserProvider

    ) {
        return new SaleService(repository, vehicleService, authenticatedUserProvider);
    }
}
