package com.deivimotors.adapters.in.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record VehicleRequest(

        @NotBlank
        String brand,

        @NotBlank
        String model,

        @NotNull
        @Positive
        Integer year,

        @NotBlank
        String color,

        @NotNull
        @Positive
        BigDecimal price

) {
}
