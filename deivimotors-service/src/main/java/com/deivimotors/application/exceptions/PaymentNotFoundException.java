package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class PaymentNotFoundException extends PaymentException {
    public PaymentNotFoundException(String message) {
        super(message, ErrorTypeEnum.NOT_FOUND);
    }
}
