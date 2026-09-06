package com.deivimotors.adapters.out.repository.mapper;

import com.deivimotors.adapters.out.repository.entity.PaymentEntity;
import com.deivimotors.domain.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentEntityMapper {

    Payment toPayment(PaymentEntity paymentEntity);

    PaymentEntity toPaymentEntity(Payment payment);
}
