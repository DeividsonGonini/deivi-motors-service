package com.deivimotors.application.ports.out;

import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryOutputPort {
    UUID save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID idVehicle) throws VehicleNotFoundException;

    Vehicle update(Vehicle vehicle);

    List<Vehicle> findBySituacaoOrderByPrecoAsc(VehicleStatusEnum status);
}
