package com.deivimotors.application.service;

import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.SaleStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class SaleService implements SaleServiceInputPort {
    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    private SaleRepositoryOutputPort repository;
    private VehicleServiceInputPort vehicleService;

    public SaleService(SaleRepositoryOutputPort repository,
                       VehicleServiceInputPort vehicleService) {
        this.repository = repository;
        this.vehicleService = vehicleService;
    }

    @Override
    public Sale create(Sale sale) {
        UUID id = UUID.randomUUID();
        sale.setId(id);
        sale.setStatus(SaleStatusEnum.EM_ANDAMENTO);
        sale.setDateTimeSale(LocalDateTime.now());

        //TODO Validar veiculo existente

        // TODO Validar veiculo a venda
        Vehicle vehicle = vehicleService.getById(sale.getVehicle().getId());

        UUID idSale = repository.save(sale);
        logger.info("Sale Created for id: {} and client: {}", idSale, sale.getClient());

        sale.setVehicle(vehicle);

        return sale;
    }

    @Override
    public Sale getById(UUID saleId) {
        return null;
    }

    @Override
    public Sale update(UUID saleId, Sale sale) {
        return null;
    }
}
