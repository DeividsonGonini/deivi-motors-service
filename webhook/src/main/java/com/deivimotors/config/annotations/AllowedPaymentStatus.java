package com.deivimotors.config.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedPaymentStatusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedPaymentStatus {
    String message() default "Invalid status. Only PAGAMENTO_APROVADO or PAGAMENTO_RECUSADO are allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}