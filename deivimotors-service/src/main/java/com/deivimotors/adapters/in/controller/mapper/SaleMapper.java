package com.deivimotors.adapters.in.controller.mapper;

import com.deivimotors.adapters.in.controller.request.SaleRequest;
import com.deivimotors.adapters.in.controller.response.SaleResponse;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "vehicle", source = "vehicleId")
    Sale toSale(SaleRequest request);

    SaleResponse toSaleResponse(Sale sale);

    default Vehicle map(UUID vehicleId) {
        return vehicleId == null ? null : Vehicle.fromId(vehicleId);
    }
}
