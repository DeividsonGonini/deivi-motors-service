package com.deivimotors.adapters.out.client.mapper;

import com.deivimotors.adapters.out.client.request.CheckoutSaleRequest;
import com.deivimotors.domain.CallbackPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckoutSaleMapper {

    CheckoutSaleRequest toCheckoutSaleRequest(CallbackPayment callbackPayment);
}
