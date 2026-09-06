package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public abstract class VehicleException extends RuntimeException {
    private final ErrorTypeEnum errorType;

    protected VehicleException(String message, ErrorTypeEnum errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }
}
