package com.deivimotors.adapters.out;

import com.deivimotors.adapters.out.client.CheckoutSaleClient;
import com.deivimotors.adapters.out.client.mapper.CheckoutSaleMapper;
import com.deivimotors.adapters.out.client.request.CheckoutSaleRequest;
import com.deivimotors.domain.CallbackPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackPaymentAdapterTest {

    @Mock
    private CheckoutSaleClient checkoutSaleClient;

    @Mock
    private CheckoutSaleMapper checkoutSaleMapper;

    @InjectMocks
    private CallbackPaymentAdapter adapter;

    private CallbackPayment callbackPayment;
    private CheckoutSaleRequest checkoutSaleRequest;
    private UUID saleId;

    @BeforeEach
    void setUp() {
        saleId = UUID.randomUUID();

        callbackPayment = new CallbackPayment();
        callbackPayment.setId(saleId);

        checkoutSaleRequest = mock(CheckoutSaleRequest.class);
    }

    @Test
    void shouldCheckoutSaleSuccessfully() {

        when(checkoutSaleMapper.toCheckoutSaleRequest(callbackPayment))
                .thenReturn(checkoutSaleRequest);

        adapter.checkoutSale(callbackPayment);

        verify(checkoutSaleMapper)
                .toCheckoutSaleRequest(callbackPayment);

        verify(checkoutSaleClient)
                .checkoutSale(saleId, checkoutSaleRequest);
    }
}

