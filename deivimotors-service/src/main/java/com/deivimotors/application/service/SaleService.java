package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.SaleStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.deivimotors.domain.enums.VehicleStatusEnum.VENDIDO;

public class SaleService implements SaleServiceInputPort {
    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    private final SaleRepositoryOutputPort repository;
    private final VehicleServiceInputPort vehicleService;

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

        Vehicle vehicle = vehicleService.getById(sale.getVehicle().getId());

        vehicleService.validationVehicleForSale(vehicle.getStatus());

        UUID idSale = repository.save(sale);
        logger.info("Sale Created for id: {} and client: {}", idSale, sale.getClient());

        sale.setVehicle(vehicle);

        return sale;
    }

    @Override
    public Sale findById(UUID saleId) {
        Optional<Sale> optionalSale = Optional.ofNullable(repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found for id: : " + saleId)));

        Sale sale = optionalSale.get();

        Vehicle vehicle = vehicleService.getById(sale.getVehicle().getId());

        sale.setVehicle(vehicle);

        return sale;
    }

    @Override
    public void updateStatus(UUID saleId, SaleStatusEnum status) throws SaleUnprocessableEntityException {
        var sale = findById(saleId);

        if (sale.getStatus().equals(SaleStatusEnum.CONCLUIDO) ||
                sale.getStatus().equals(SaleStatusEnum.CANCELADO)) {
            throw new SaleUnprocessableEntityException(
                    "The sale cannot be changed, as it has already been finalized. Current sale status : " + sale.getStatus());
        }

        switch (status) {
            case EM_ANDAMENTO -> throw new SaleUnprocessableEntityException(
                    "Invalid status sequence for this update. Requested status: " + status);
            case CONCLUIDO -> {
                Sale saleUpdated = sale.completed();
                repository.save(saleUpdated);
                vehicleService.updateStatus(sale.getVehicle().getId(), VENDIDO);
            }
            case CANCELADO -> {
                Sale saleUpdated = sale.canceled();
                repository.save(saleUpdated);
            }
            default -> throw new SaleUnprocessableEntityException(
                    "The current sale status: " + sale.getStatus() + " cannot be completed");
        }
    }

    @Override
    public Sale update(UUID saleId, Sale sale) {
        return null;
    }

}
