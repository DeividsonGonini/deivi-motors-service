package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class SaleUnprocessableEntityException extends SaleException {

    public SaleUnprocessableEntityException(String message) {
        super(message, ErrorTypeEnum.UNPROCESSABLE_ENTITY);
    }
}
