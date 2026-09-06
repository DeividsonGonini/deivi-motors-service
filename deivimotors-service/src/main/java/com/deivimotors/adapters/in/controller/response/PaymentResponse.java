package com.deivimotors.adapters.in.controller.response;

import com.deivimotors.domain.enums.PaymentStatusEnum;

import java.util.UUID;

public record PaymentResponse(
        String id,
        UUID saleId,
        PaymentStatusEnum status
) {
}
