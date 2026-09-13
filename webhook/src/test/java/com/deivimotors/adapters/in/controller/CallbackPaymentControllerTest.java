package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.CallbackPaymentMapper;
import com.deivimotors.adapters.in.controller.request.CallbackPaymentRequest;
import com.deivimotors.domain.CallbackPayment;
import com.deivimotors.domain.PaymentStatusEnum;
import com.deivimotors.domain.ports.in.CallbackPaymentInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackPaymentControllerTest {

    @Mock
    private CallbackPaymentInputPort callbackPaymentInputPort;

    @Mock
    private CallbackPaymentMapper callbackPaymentMapper;

    @InjectMocks
    private CallbackPaymentController controller;

    private CallbackPaymentRequest request;
    private CallbackPayment callbackPayment;
    private UUID paymentId;

    @BeforeEach
    void setUp() {
        paymentId = UUID.randomUUID();

        request = new CallbackPaymentRequest(PaymentStatusEnum.PAGAMENTO_APROVADO);

        callbackPayment = new CallbackPayment();
    }

    @Test
    void shouldReceiveCallbackPaymentSuccessfully() {

        when(callbackPaymentMapper.toCallbackPayment(request))
                .thenReturn(callbackPayment);

        ResponseEntity<Void> response =
                controller.callBackPayment(request, paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertEquals(paymentId, callbackPayment.getId());

        verify(callbackPaymentMapper)
                .toCallbackPayment(request);

        verify(callbackPaymentInputPort)
                .callbackPayment(callbackPayment);
    }

    @Test
    void shouldSetPathIdOnCallbackPayment() {

        UUID pathId = UUID.randomUUID();

        when(callbackPaymentMapper.toCallbackPayment(request))
                .thenReturn(callbackPayment);

        controller.callBackPayment(request, pathId);

        assertEquals(pathId, callbackPayment.getId());

        verify(callbackPaymentInputPort)
                .callbackPayment(callbackPayment);
    }

}
