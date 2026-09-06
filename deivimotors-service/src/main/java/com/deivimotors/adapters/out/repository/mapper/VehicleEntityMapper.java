package com.deivimotors.adapters.out.repository.mapper;

import com.deivimotors.adapters.out.repository.entity.VehicleEntity;
import com.deivimotors.domain.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleEntityMapper {
    Vehicle toVehicle(VehicleEntity vehicleEntity);

    VehicleEntity toVehicleEntity(Vehicle vehicle);
}
