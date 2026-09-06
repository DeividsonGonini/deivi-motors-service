package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public abstract class PaymentException extends RuntimeException {
    private final ErrorTypeEnum errorType;

    protected PaymentException(String message, ErrorTypeEnum errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }
}
