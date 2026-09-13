package com.deivimotors.adapters.in.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record SaleRequest(
        @NotNull
        UUID vehicleId,
        @CPF
        String customerCpf
) {
}
