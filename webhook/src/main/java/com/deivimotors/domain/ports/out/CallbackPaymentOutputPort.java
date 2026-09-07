package com.deivimotors.domain.ports.out;

import com.deivimotors.domain.CallbackPayment;

public interface CallbackPaymentOutputPort {

    void checkoutSale(CallbackPayment callBackPayment);
}
