package com.deivimotors.adapters.in.controller.mapper;

import com.deivimotors.adapters.in.controller.request.PaymentRequest;
import com.deivimotors.adapters.in.controller.response.PaymentResponse;
import com.deivimotors.domain.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toPayment(PaymentRequest paymentRequest);

    PaymentResponse toPaymentResponse(Payment payment);
}
