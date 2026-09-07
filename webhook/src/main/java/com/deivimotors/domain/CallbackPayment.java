package com.deivimotors.domain;

import java.util.UUID;

public class CallbackPayment {
    UUID id;
    PaymentStatusEnum status;

    public CallbackPayment(UUID id, PaymentStatusEnum status) {
        this.id = id;
        this.status = status;
    }

    public CallbackPayment() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PaymentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusEnum status) {
        this.status = status;
    }
}
