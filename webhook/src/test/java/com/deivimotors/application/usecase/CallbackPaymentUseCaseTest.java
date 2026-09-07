package com.deivimotors.application.usecase;

import com.deivimotors.domain.CallbackPayment;
import com.deivimotors.domain.PaymentStatusEnum;
import com.deivimotors.domain.ports.out.CallbackPaymentOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CallbackPaymentUseCaseTest {

    @InjectMocks
    private CallbackPaymentUseCase useCase;

    @Mock
    private CallbackPaymentOutputPort callbackPaymentOutputPort;

    @Test
    void callbackPayment_shouldSendToOutputPort_whenRequestIsValid() {
        CallbackPayment callbackPayment = new CallbackPayment(UUID.randomUUID(), PaymentStatusEnum.PAGAMENTO_APROVADO);

        useCase.callbackPayment(callbackPayment);

        verify(callbackPaymentOutputPort).checkoutSale(callbackPayment);
    }

    @Test
    void callbackPayment_shouldThrowNullPointerException_whenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.callbackPayment(null));

        verifyNoInteractions(callbackPaymentOutputPort);
    }
}
