package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class SaleNotFoundException extends SaleException {

    public SaleNotFoundException(String message) {
        super(message, ErrorTypeEnum.NOT_FOUND);
    }
}
