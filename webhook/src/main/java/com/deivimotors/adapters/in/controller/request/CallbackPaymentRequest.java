package com.deivimotors.adapters.in.controller.request;

import com.deivimotors.config.annotations.AllowedPaymentStatus;
import com.deivimotors.domain.PaymentStatusEnum;
import jakarta.validation.constraints.NotNull;

public record CallbackPaymentRequest(

        @NotNull
        @AllowedPaymentStatus
        PaymentStatusEnum status
) {
}