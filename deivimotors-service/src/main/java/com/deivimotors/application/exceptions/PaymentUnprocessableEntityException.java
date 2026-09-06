package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class PaymentUnprocessableEntityException extends PaymentException {
    public PaymentUnprocessableEntityException(String message) {
        super(message, ErrorTypeEnum.UNPROCESSABLE_ENTITY);
    }
}
