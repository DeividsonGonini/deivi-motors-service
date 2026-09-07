package com.deivimotors.domain.enums;

public enum VehicleStatusEnum {
    A_VENDA("A_VENDA"),
    VENDIDO("VENDIDO"),
    INATIVO("INATIVO");

    private String status;

    VehicleStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
