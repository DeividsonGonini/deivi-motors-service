package com.deivimotors.adapters.in.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record VehicleRequest(

        @NotBlank
        String marca,

        @NotBlank
        String modelo,

        @NotNull
        @Positive
        Integer ano,

        @NotBlank
        String cor,

        @NotNull
        @Positive
        BigDecimal preco

//        @NotNull
//        VehicleStatusEnum situacao
) {
}
