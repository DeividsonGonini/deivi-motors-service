package com.deivimotors.domain.ports.in;

import com.deivimotors.domain.CallbackPayment;

public interface CallbackPaymentInputPort {

    void callbackPayment(CallbackPayment callBackPayment);
}
