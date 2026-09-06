package com.deivimotors.domain.enums;

public enum PaymentStatusEnum {
    AGUARDANDO_PAGAMENTO("AGUARDANDO_PAGAMENTO"),
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