package com.deivimotors.adapters.in.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaleRequest(
        @NotNull
        UUID vehicleId,
        @NotBlank
        String client
) {
}
