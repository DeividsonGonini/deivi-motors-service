package com.deivimotors.application.ports.in;

import com.deivimotors.application.exceptions.PaymentException;
import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.domain.Payment;
import com.deivimotors.domain.enums.PaymentStatusEnum;

import java.util.UUID;

public interface PaymentServiceInputPort {

    Payment create(UUID saleId) throws PaymentNotFoundException;

    Payment findPaymentBySaleId(UUID saleId) throws PaymentNotFoundException;

    void checkout(UUID saleId, PaymentStatusEnum status) throws PaymentException;
}
