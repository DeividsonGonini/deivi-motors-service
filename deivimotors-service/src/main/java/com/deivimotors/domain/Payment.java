package com.deivimotors.domain;


import com.deivimotors.domain.enums.PaymentStatusEnum;
import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;


import java.io.Serializable;
import java.util.UUID;

public class Payment implements Serializable {

    private String id;
    private UUID saleId;
    private PaymentStatusEnum status;

    public Payment(UUID saleId) {
        this.id = null;
        this.saleId = saleId;
        this.status = PaymentStatusEnum.AGUARDANDO_PAGAMENTO;
    }

    public Payment(String id, UUID saleId, PaymentStatusEnum status) {
        this.id = id;
        this.saleId = saleId;
        this.status = status;
    }

    public Payment() {
    }


    public Payment approve() throws PaymentUnprocessableEntityException {
        if (this.status != PaymentStatusEnum.AGUARDANDO_PAGAMENTO) {
            throw new PaymentUnprocessableEntityException("The current status: " + this.status.getStatus() +
                    " does not allow payment approval.");
        }
        return new Payment(this.id,
                this.saleId,
                PaymentStatusEnum.PAGAMENTO_APROVADO);
    }

    public Payment reject() {
        return new Payment(this.id,
                this.saleId,
                PaymentStatusEnum.PAGAMENTO_RECUSADO);
    }

    public String getId() {
        return id;
    }

    public UUID getSaleId() {
        return saleId;
    }

    public PaymentStatusEnum getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSaleId(UUID saleId) {
        this.saleId = saleId;
    }

    public void setStatus(PaymentStatusEnum status) {
        this.status = status;
    }
}
