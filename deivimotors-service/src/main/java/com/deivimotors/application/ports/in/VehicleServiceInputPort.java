package com.deivimotors.application.ports.in;

import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.SaleStatusEnum;
import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.util.List;
import java.util.UUID;

public interface VehicleServiceInputPort {
    UUID create(Vehicle vehicle);

    Vehicle getById(UUID vehicleId) throws VehicleNotFoundException;

    Vehicle update(UUID vehicleId, Vehicle vehicle) throws VehicleNotFoundException;

    List<Vehicle> findBySituacaoOrderByPrecoAsc(VehicleStatusEnum status);

    void validationVehicleForSale(VehicleStatusEnum status);

    void updateStatus(UUID vehicleId, VehicleStatusEnum status);
}
