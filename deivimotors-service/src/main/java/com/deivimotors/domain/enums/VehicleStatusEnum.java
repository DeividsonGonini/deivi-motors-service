package com.deivimotors.domain.enums;

public enum VehicleStatusEnum {
    A_VENDA("à venda"),
    VENDIDO("vendido");

    private String status;

    VehicleStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
