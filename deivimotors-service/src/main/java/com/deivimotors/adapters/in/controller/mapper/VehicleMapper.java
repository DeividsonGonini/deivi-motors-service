package com.deivimotors.adapters.in.controller.mapper;

import com.deivimotors.adapters.in.controller.request.VehicleRequest;
import com.deivimotors.adapters.in.controller.response.VehicleResponse;
import com.deivimotors.domain.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicle(VehicleRequest vehicleRequest);
    VehicleResponse toVehicleResponse(Vehicle vehicle);

}
