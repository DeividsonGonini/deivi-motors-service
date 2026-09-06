package com.deivimotors.application.ports.out;

import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryOutputPort {

    UUID save(Payment payment) throws PaymentNotFoundException;

    Optional<Payment> getBySaleId(UUID saleId) throws PaymentNotFoundException;

}
