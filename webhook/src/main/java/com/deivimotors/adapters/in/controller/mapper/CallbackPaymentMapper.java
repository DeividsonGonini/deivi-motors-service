package com.deivimotors.adapters.in.controller.mapper;

import com.deivimotors.adapters.in.controller.request.CallbackPaymentRequest;
import com.deivimotors.domain.CallbackPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CallbackPaymentMapper {
    CallbackPayment toCallbackPayment(CallbackPaymentRequest callBackPaymentRequest);
}
