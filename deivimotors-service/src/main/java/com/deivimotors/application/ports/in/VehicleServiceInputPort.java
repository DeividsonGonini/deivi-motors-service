package com.deivimotors.application.ports.in;

import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.util.List;

public interface VehicleServiceInputPort {
    String create(Vehicle vehicle);

    Vehicle getById(String vehicleId) throws VehicleNotFoundException;

    Vehicle update(String vehicleId, Vehicle vehicle) throws VehicleNotFoundException;

    List<Vehicle> findBySituacaoOrderByPrecoAsc(VehicleStatusEnum status);
}
