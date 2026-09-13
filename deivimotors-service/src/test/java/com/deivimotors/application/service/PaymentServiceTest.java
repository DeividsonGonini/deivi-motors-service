package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.application.ports.out.PaymentRepositoryOutputPort;
import com.deivimotors.domain.Payment;
import com.deivimotors.domain.enums.PaymentStatusEnum;
import com.deivimotors.domain.enums.SaleStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepositoryOutputPort repository;

    @Mock
    private SaleServiceInputPort saleService;

    private PaymentService paymentService;

    private UUID saleId;
    private UUID paymentId;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(repository, saleService);

        saleId = UUID.randomUUID();
        paymentId = UUID.randomUUID();
    }

    // CREATE
    @Test
    void shouldReturnExistingPaymentWhenCreatingPayment() {

        Payment existingPayment = new Payment(paymentId, saleId, PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(existingPayment));

        Payment result = paymentService.create(saleId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(saleId, result.getSaleId());
        assertEquals(PaymentStatusEnum.AGUARDANDO_PAGAMENTO, result.getStatus());

        verify(repository).getBySaleId(saleId);
        verify(repository, never()).save(any(Payment.class));
    }

    @Test
    void shouldCreateNewPaymentWhenPaymentDoesNotExist() {

        when(repository.getBySaleId(saleId)).thenReturn(Optional.empty());

        when(repository.save(any(Payment.class))).thenReturn(paymentId);

        Payment result = paymentService.create(saleId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(saleId, result.getSaleId());
        assertEquals(PaymentStatusEnum.AGUARDANDO_PAGAMENTO, result.getStatus());

        verify(repository).getBySaleId(saleId);
        verify(repository).save(any(Payment.class));
    }

    // FIND PAYMENT
    @Test
    void shouldFindPaymentBySaleId() {

        Payment payment = new Payment(paymentId, saleId, PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        Payment result = paymentService.findPaymentBySaleId(saleId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(saleId, result.getSaleId());
        assertEquals(PaymentStatusEnum.AGUARDANDO_PAGAMENTO, result.getStatus());

        verify(repository).getBySaleId(saleId);
    }

    @Test
    void shouldThrowExceptionWhenPaymentIsNotFound() {

        when(repository.getBySaleId(saleId)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> paymentService.findPaymentBySaleId(saleId));

        assertEquals("Payment for sale with id: " + saleId + " not found", exception.getMessage());

        verify(repository).getBySaleId(saleId);
    }

    // CHECKOUT - PAGAMENTO APROVADO
    @Test
    void shouldApprovePaymentAndCompleteSale() {

        Payment payment = new Payment(paymentId, saleId, PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        paymentService.checkout(saleId, PaymentStatusEnum.PAGAMENTO_APROVADO);

        verify(repository).getBySaleId(saleId);

        verify(repository).save(
                argThat(savedPayment ->
                        savedPayment.getId().equals(paymentId)
                                && savedPayment.getSaleId().equals(saleId)
                                && savedPayment.getStatus()
                                .equals(PaymentStatusEnum.PAGAMENTO_APROVADO)
                )
        );

        verify(saleService).updateStatus(saleId, SaleStatusEnum.CONCLUIDO);
    }


    // CHECKOUT - PAGAMENTO RECUSADO
    @Test
    void shouldRejectPaymentAndCancelSale() {

        Payment payment = new Payment(paymentId, saleId, PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        paymentService.checkout(saleId, PaymentStatusEnum.PAGAMENTO_RECUSADO);

        verify(repository).getBySaleId(saleId);

        verify(repository).save(
                argThat(savedPayment ->
                        savedPayment.getId().equals(paymentId)
                                && savedPayment.getSaleId().equals(saleId)
                                && savedPayment.getStatus()
                                .equals(PaymentStatusEnum.PAGAMENTO_RECUSADO)
                )
        );

        verify(saleService).updateStatus(saleId, SaleStatusEnum.CANCELADO);
    }

    // CHECKOUT - AGUARDANDO PAGAMENTO
    @Test
    void shouldNotAllowCheckoutWithWaitingPaymentStatus() {

        Payment payment = new Payment(paymentId, saleId, PaymentStatusEnum.AGUARDANDO_PAGAMENTO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> paymentService.checkout(
                                saleId,
                                PaymentStatusEnum.AGUARDANDO_PAGAMENTO
                        )
                );

        assertEquals(
                "Invalid status sequence for this update. Requested status: "
                        + PaymentStatusEnum.AGUARDANDO_PAGAMENTO, exception.getMessage()
        );

        verify(repository).getBySaleId(saleId);
        verify(repository, never()).save(any(Payment.class));
        verifyNoInteractions(saleService);
    }

    // CHECKOUT - PAGAMENTO JÁ APROVADO
    @Test
    void shouldNotAllowCheckoutWhenPaymentIsAlreadyApproved() {

        Payment payment = new Payment(paymentId,saleId,PaymentStatusEnum.PAGAMENTO_APROVADO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> paymentService.checkout(
                                saleId,
                                PaymentStatusEnum.PAGAMENTO_RECUSADO
                        )
                );

        assertEquals(
                "The payment cannot be changed, as it has already been finalized. "
                        + "Current payment status : "
                        + PaymentStatusEnum.PAGAMENTO_APROVADO,
                exception.getMessage()
        );

        verify(repository).getBySaleId(saleId);
        verify(repository, never()).save(any(Payment.class));
        verifyNoInteractions(saleService);
    }

    // CHECKOUT - PAGAMENTO JÁ RECUSADO
    @Test
    void shouldNotAllowCheckoutWhenPaymentIsAlreadyRejected() {

        Payment payment = new Payment(paymentId,saleId,PaymentStatusEnum.PAGAMENTO_RECUSADO);

        when(repository.getBySaleId(saleId)).thenReturn(Optional.of(payment));

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> paymentService.checkout(saleId,PaymentStatusEnum.PAGAMENTO_APROVADO)
                );

        assertEquals(
                "The payment cannot be changed, as it has already been finalized. "
                        + "Current payment status : "
                        + PaymentStatusEnum.PAGAMENTO_RECUSADO,
                exception.getMessage()
        );

        verify(repository).getBySaleId(saleId);
        verify(repository, never()).save(any(Payment.class));
        verifyNoInteractions(saleService);
    }

    // CHECKOUT - PAGAMENTO NÃO ENCONTRADO
    @Test
    void shouldPropagateExceptionWhenPaymentIsNotFoundDuringCheckout() {

        when(repository.getBySaleId(saleId)).thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.checkout(saleId,PaymentStatusEnum.PAGAMENTO_APROVADO)
        );

        verify(repository).getBySaleId(saleId);
        verify(repository, never()).save(any(Payment.class));
        verifyNoInteractions(saleService);
    }
}