package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.PaymentMapper;
import com.deivimotors.adapters.in.controller.request.PaymentRequest;
import com.deivimotors.adapters.in.controller.response.PaymentResponse;
import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.domain.Payment;
import com.deivimotors.domain.enums.PaymentStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentServiceInputPort service;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentController controller;

    private UUID saleId;
    private UUID paymentId;
    private Payment payment;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        controller = new PaymentController(service, paymentMapper);

        saleId = UUID.randomUUID();
        paymentId = UUID.randomUUID();

        payment = new Payment(paymentId,saleId,PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        paymentResponse = new PaymentResponse(paymentId,saleId,PaymentStatusEnum.AGUARDANDO_PAGAMENTO);
    }

    @Test
    void shouldFindPaymentStatusBySaleId() throws PaymentNotFoundException {
        when(service.findPaymentBySaleId(saleId)).thenReturn(payment);
        when(paymentMapper.toPaymentResponse(payment)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = controller.findStatusPaymentOfSale(saleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentResponse, response.getBody());

        verify(service).findPaymentBySaleId(saleId);
        verify(paymentMapper).toPaymentResponse(payment);
    }

    @Test
    void shouldPropagateExceptionWhenPaymentIsNotFound() throws PaymentNotFoundException {
        when(service.findPaymentBySaleId(saleId)).thenThrow(new PaymentNotFoundException("Payment not found"));

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> controller.findStatusPaymentOfSale(saleId)
        );

        assertEquals("Payment not found", exception.getMessage());

        verify(service).findPaymentBySaleId(saleId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void shouldCreatePayment() throws PaymentNotFoundException {
        when(service.create(saleId)).thenReturn(payment);
        when(paymentMapper.toPaymentResponse(payment)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = controller.createPayment(saleId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentResponse, response.getBody());

        assertNotNull(response.getHeaders().getLocation());
        assertEquals(
                "/payments/sale/" + saleId,
                response.getHeaders().getLocation().toString()
        );

        verify(service).create(saleId);
        verify(paymentMapper).toPaymentResponse(payment);
    }

    @Test
    void shouldPropagateExceptionWhenCreatingPaymentFails() throws PaymentNotFoundException {
        when(service.create(saleId)).thenThrow(new PaymentNotFoundException("Payment not found"));

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> controller.createPayment(saleId)
        );

        assertEquals("Payment not found", exception.getMessage());

        verify(service).create(saleId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void shouldCheckoutPaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest(PaymentStatusEnum.PAGAMENTO_APROVADO);

        ResponseEntity<Void> response = controller.checkout(saleId, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(service).checkout(saleId,PaymentStatusEnum.PAGAMENTO_APROVADO);
    }

    @Test
    void shouldPropagateExceptionWhenCheckoutFails() throws Exception {
        PaymentRequest request = new PaymentRequest(PaymentStatusEnum.PAGAMENTO_APROVADO);

        doThrow(new PaymentNotFoundException("Payment not found"))
                .when(service)
                .checkout(saleId, PaymentStatusEnum.PAGAMENTO_APROVADO);

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> controller.checkout(saleId, request)
        );

        assertEquals("Payment not found", exception.getMessage());

        verify(service).checkout(saleId, PaymentStatusEnum.PAGAMENTO_APROVADO);
    }
}
