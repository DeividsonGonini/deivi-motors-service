package com.deivimotors.adapters.in.controller.response;

import com.deivimotors.domain.enums.SaleStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record SaleResponse(
        UUID id,
        VehicleResponse vehicle,
        String customerCpf,
        SaleStatusEnum status,
        LocalDateTime dateTimeSale
) {
}
