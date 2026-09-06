package com.deivimotors.adapters.in.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SaleRequest(
        @NotBlank
        String vehicleId,
        @NotBlank
        String client
) {
}
