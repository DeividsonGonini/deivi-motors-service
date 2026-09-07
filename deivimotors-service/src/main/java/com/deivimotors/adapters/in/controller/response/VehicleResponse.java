package com.deivimotors.adapters.in.controller.response;

import com.deivimotors.domain.enums.VehicleStatusEnum;
import java.math.BigDecimal;

public record VehicleResponse(

        String id,
        String brand,
        String model,
        Integer year,
        String color,
        BigDecimal price,
        VehicleStatusEnum status

) {
}
