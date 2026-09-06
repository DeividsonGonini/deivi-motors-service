package com.deivimotors.adapters.out.repository;

import com.deivimotors.adapters.out.repository.entity.PaymentEntity;
import com.deivimotors.adapters.out.repository.mapper.PaymentEntityMapper;
import com.deivimotors.adapters.out.repository.mongo.PaymentMongoRepository;
import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.ports.out.PaymentRepositoryOutputPort;
import com.deivimotors.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryOutputPort {
    private final PaymentMongoRepository mongoRepository;
    private final PaymentEntityMapper mapper;

    @Override
    public String save(Payment payment) throws PaymentNotFoundException {
        var paymentEntity = mapper.toPaymentEntity(payment);
        PaymentEntity paymentSave = mongoRepository.save(paymentEntity);

        return paymentSave.getId();
    }

    @Override
    public Optional<Payment> getBySaleId(UUID saleId) throws PaymentNotFoundException {
        return mongoRepository.findBySaleId(saleId)
                .map(mapper::toPayment);
    }
}
