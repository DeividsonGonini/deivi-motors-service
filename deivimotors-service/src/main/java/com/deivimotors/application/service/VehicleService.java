package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.exceptions.VehicleUnprocessableEntityException;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.application.ports.out.VehicleRepositoryOutputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleService implements VehicleServiceInputPort {
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
    private final VehicleRepositoryOutputPort repository;

    public VehicleService(VehicleRepositoryOutputPort repository) {
        this.repository = repository;
    }

    @Override
    public UUID create(Vehicle vehicle) {

        UUID id = UUID.randomUUID();
        vehicle.setId(id);
        vehicle.setStatus(VehicleStatusEnum.A_VENDA);

        UUID idVehicle = repository.save(vehicle);
        return idVehicle;
    }

    @Override
    public Vehicle getById(UUID vehicleId) throws VehicleNotFoundException {
        Optional<Vehicle> vehicle = Optional.ofNullable(repository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: " + vehicleId)));

        return vehicle.get();
    }

    @Override
    public Vehicle update(UUID vehicleId, Vehicle vehicle) throws VehicleNotFoundException {
        Vehicle currentVehicle = findById(vehicleId);

        if (currentVehicle.getStatus().equals(VehicleStatusEnum.VENDIDO)) {
            throw new PaymentUnprocessableEntityException("Vehicle sold, can't updated");
        }

        Vehicle updateVehicle = updateFrom(currentVehicle, vehicle);

        repository.update(updateVehicle);

        return updateVehicle;
    }

    @Override
    public List<Vehicle> findBySituacaoOrderByPrecoAsc(VehicleStatusEnum status) {
        return repository.findByStatusOrderByPriceAsc(status);
    }

    @Override
    public void validationVehicleForSale(VehicleStatusEnum status) {
        if (!status.equals(VehicleStatusEnum.A_VENDA)) {
            throw new PaymentUnprocessableEntityException("vehicle sold");
        }
        logger.info("Vehicle available for sale");
    }

    private Vehicle findById(UUID vehicleId) throws VehicleNotFoundException {
        Optional<Vehicle> vehicle = Optional.ofNullable(repository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: : " + vehicleId)));
        return vehicle.get();
    }

    @Override
    public void updateStatus(UUID vehicleId, VehicleStatusEnum status) {
        var vehicle = findById(vehicleId);

        if (vehicle.getStatus().equals(VehicleStatusEnum.VENDIDO)) {
            throw new VehicleUnprocessableEntityException(
                    "The vehicle cannot be changed, as it has already been finalized. Current sale status : " + vehicle.getStatus());
        }

        switch (status) {
            case A_VENDA -> throw new VehicleUnprocessableEntityException(
                    "Invalid status sequence for this update. Requested status: " + status);
            case VENDIDO -> {
                Vehicle vehicleUpdated = vehicle.sold();
                repository.save(vehicleUpdated);
            }
            case INATIVO -> {
                Vehicle vehicleUpdated = vehicle.inactive();
                repository.save(vehicleUpdated);
            }
            default -> throw new VehicleUnprocessableEntityException(
                    "The vehicle's current status: " + vehicle.getStatus() + " cannot be changed");
        }
    }

    private Vehicle updateFrom(Vehicle currentVehicle, Vehicle vehicleToUpdate) {
        if (vehicleToUpdate.getBrand() != null) currentVehicle.setBrand(vehicleToUpdate.getBrand());
        if (vehicleToUpdate.getModel() != null) currentVehicle.setModel(vehicleToUpdate.getModel());
        if (vehicleToUpdate.getYear() != null) currentVehicle.setYear(vehicleToUpdate.getYear());
        if (vehicleToUpdate.getColor() != null) currentVehicle.setColor(vehicleToUpdate.getColor());
        if (vehicleToUpdate.getPrice() != null) currentVehicle.setPrice(vehicleToUpdate.getPrice());
        if (vehicleToUpdate.getStatus() != null) currentVehicle.setStatus(vehicleToUpdate.getStatus());
        return currentVehicle;
    }
}
