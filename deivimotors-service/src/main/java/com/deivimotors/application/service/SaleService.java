package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.config.security.AuthenticatedUserProvider;
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
    private final AuthenticatedUserProvider authenticatedUserProvider;


    public SaleService(SaleRepositoryOutputPort repository,
                       VehicleServiceInputPort vehicleService,
                       AuthenticatedUserProvider authenticatedUserProvider) {
        this.repository = repository;
        this.vehicleService = vehicleService;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    public Sale create(Sale sale) throws SaleUnprocessableEntityException {
        UUID id = UUID.randomUUID();
        sale.setId(id);
        sale.setStatus(SaleStatusEnum.EM_ANDAMENTO);
        sale.setDateTimeSale(LocalDateTime.now());

        /**
         * Caso tenha usuário autenticado, seta o CPF do cliente recuperado do token
         * Caso não, utiliza o cliente passado na requisição
         */
        authenticatedUserProvider.getCurrentUser()
                .ifPresent(user -> sale.setCustomerCpf(user.cpf()));

        if (sale.getCustomerCpf() == null || sale.getCustomerCpf().isBlank()) {
            throw new SaleUnprocessableEntityException(
                    "Customer CPF was not provided"
            );
        }

        Vehicle vehicle = vehicleService.getById(sale.getVehicle().getId());
        vehicleService.validationVehicleForSale(vehicle.getStatus());

        UUID idSale = repository.save(sale);
        logger.info("Sale Created for id: {} and customerCpf: {}", idSale, sale.getCustomerCpf());

        sale.setVehicle(vehicle);

        return sale;
    }

    @Override
    public Sale findById(UUID saleId) throws SaleNotFoundException, SaleUnprocessableEntityException {
        Optional<Sale> optionalSale = Optional.ofNullable(repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found for id: : " + saleId)));

        Sale sale = optionalSale.get();

        authenticatedUserProvider.getCurrentUser()
                .ifPresent(user -> {
                    if (!user.cpf().equals(sale.getCustomerCpf())) {
                        throw new SaleUnprocessableEntityException(
                                "The sale does not belong to the requested customer"
                        );
                    }
                });

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
