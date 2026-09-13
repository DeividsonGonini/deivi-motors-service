package com.deivimotors.application.usecase;

import com.deivimotors.domain.CallbackPayment;
import com.deivimotors.application.ports.in.CallbackPaymentInputPort;
import com.deivimotors.application.ports.out.CallbackPaymentOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackPaymentUseCase implements CallbackPaymentInputPort {
    private static final Logger logger = LoggerFactory.getLogger(CallbackPaymentUseCase.class);

    private final CallbackPaymentOutputPort callbackPaymentOutputPort;

    public CallbackPaymentUseCase(CallbackPaymentOutputPort callbackPaymentOutputPort) {
        this.callbackPaymentOutputPort = callbackPaymentOutputPort;
    }

    @Override
    public void callbackPayment(CallbackPayment callBackPayment) {
        logger.info("Starting checkout Sale: {}, Status: {}", callBackPayment.getId(), callBackPayment.getStatus());
        callbackPaymentOutputPort.checkoutSale(callBackPayment);
        logger.info("Completed checkout Sale: {}, Status: {}", callBackPayment.getId(), callBackPayment.getStatus());
    }
}
