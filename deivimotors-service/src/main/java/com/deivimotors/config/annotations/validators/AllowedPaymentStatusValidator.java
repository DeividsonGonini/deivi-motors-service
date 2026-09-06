package com.deivimotors.config.annotations.validators;


import com.deivimotors.domain.enums.PaymentStatusEnum;
import com.deivimotors.config.annotations.AllowedPaymentStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;

public class AllowedPaymentStatusValidator implements ConstraintValidator<AllowedPaymentStatus, PaymentStatusEnum> {

    private static final EnumSet<PaymentStatusEnum> ALLOWED_STATUSES =
            EnumSet.of(PaymentStatusEnum.PAGAMENTO_APROVADO, PaymentStatusEnum.PAGAMENTO_RECUSADO);

    @Override
    public boolean isValid(PaymentStatusEnum value, ConstraintValidatorContext context) {
        return value != null && ALLOWED_STATUSES.contains(value);
    }

}
