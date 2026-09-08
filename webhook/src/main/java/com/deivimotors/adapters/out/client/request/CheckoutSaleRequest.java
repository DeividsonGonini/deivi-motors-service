package com.deivimotors.adapters.out.client.request;

import com.deivimotors.config.annotations.AllowedPaymentStatus;
import com.deivimotors.domain.PaymentStatusEnum;
import jakarta.validation.constraints.NotBlank;

public record CheckoutSaleRequest(
        @AllowedPaymentStatus
        @NotBlank
        PaymentStatusEnum status
) {
}
