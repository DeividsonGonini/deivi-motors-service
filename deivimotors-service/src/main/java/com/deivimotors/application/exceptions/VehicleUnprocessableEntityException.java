package com.deivimotors.application.exceptions;

import com.deivimotors.application.exceptions.enums.ErrorTypeEnum;

public class VehicleUnprocessableEntityException extends VehicleException {
    public VehicleUnprocessableEntityException(String message) {

        super(message, ErrorTypeEnum.UNPROCESSABLE_ENTITY);
    }
}
