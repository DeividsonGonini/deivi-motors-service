package com.deivimotors.config.annotations;

import com.deivimotors.domain.PaymentStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedPaymentStatusValidator implements ConstraintValidator<AllowedPaymentStatus, PaymentStatusEnum> {
    @Override
    public boolean isValid(PaymentStatusEnum value, ConstraintValidatorContext context) {
        return value == PaymentStatusEnum.PAGAMENTO_APROVADO ||
                value == PaymentStatusEnum.PAGAMENTO_RECUSADO;
    }
}

