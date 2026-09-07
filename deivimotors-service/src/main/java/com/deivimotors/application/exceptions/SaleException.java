package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public abstract class SaleException extends RuntimeException {

    private final ErrorTypeEnum errorType;

    protected SaleException(String message, ErrorTypeEnum errorType){
        super(message);
        this.errorType = errorType;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }
}
