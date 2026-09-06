package com.deivimotors.adapters.in.controller.request;

import com.deivimotors.domain.enums.PaymentStatusEnum;
import com.deivimotors.config.annotations.AllowedPaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull
        @AllowedPaymentStatus
        PaymentStatusEnum status
) {
}
