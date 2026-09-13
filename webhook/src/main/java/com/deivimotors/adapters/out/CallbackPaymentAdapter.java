package com.deivimotors.adapters.out;

import com.deivimotors.adapters.out.client.CheckoutSaleClient;
import com.deivimotors.adapters.out.client.mapper.CheckoutSaleMapper;
import com.deivimotors.adapters.out.client.request.CheckoutSaleRequest;
import com.deivimotors.domain.CallbackPayment;
import com.deivimotors.application.ports.out.CallbackPaymentOutputPort;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CallbackPaymentAdapter implements CallbackPaymentOutputPort {
    private static final Logger logger = LoggerFactory.getLogger(CallbackPaymentAdapter.class);

    private CheckoutSaleClient checkoutSaleClient;
    private final CheckoutSaleMapper checkoutSaleMapper;

    @Override
    public void checkoutSale(CallbackPayment callBackPayment) {

        logger.info("Starting integration for payment Sale: {}", callBackPayment.getId());

        CheckoutSaleRequest checkoutSaleRequest = checkoutSaleMapper.toCheckoutSaleRequest(callBackPayment);
        checkoutSaleClient.checkoutSale(callBackPayment.getId(), checkoutSaleRequest);

        logger.info("Completed integration for payment Sale: {}", callBackPayment.getId());
    }
}
