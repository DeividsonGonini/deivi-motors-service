package com.deivimotors.config;

import com.deivimotors.application.ports.out.VehicleRepositoryOutputPort;
import com.deivimotors.application.service.VehicleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleConfig {
    @Bean
    public VehicleService vehicleService(
            VehicleRepositoryOutputPort repository
    ){
        return new VehicleService(repository);
    }

}
