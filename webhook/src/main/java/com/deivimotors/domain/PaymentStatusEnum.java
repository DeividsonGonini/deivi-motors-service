package com.deivimotors.domain;

public enum PaymentStatusEnum {
    PAGAMENTO_APROVADO("PAGAMENTO_APROVADO"),
    PAGAMENTO_RECUSADO("PAGAMENTO_RECUSADO");

    private String status;

    PaymentStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
