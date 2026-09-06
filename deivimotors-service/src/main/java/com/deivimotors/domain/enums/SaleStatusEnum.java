package com.deivimotors.domain.enums;

public enum SaleStatusEnum {
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CANCELADO("CANCELADO"),
    CONCLUIDO("CONCLUIDO");

    private String status;

    SaleStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
