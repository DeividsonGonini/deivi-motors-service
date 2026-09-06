package com.deivimotors.adapters.in.controller.response;

import com.deivimotors.domain.enums.VehicleStatusEnum;
import java.math.BigDecimal;

public record VehicleResponse(

        String id,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco,
        VehicleStatusEnum situacao

) {
}
