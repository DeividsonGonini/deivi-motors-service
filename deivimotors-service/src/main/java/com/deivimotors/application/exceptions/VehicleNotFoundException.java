package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class VehicleNotFoundException extends PaymentException {
    public VehicleNotFoundException(String message) {
        super(message, ErrorTypeEnum.NOT_FOUND);
    }
}
