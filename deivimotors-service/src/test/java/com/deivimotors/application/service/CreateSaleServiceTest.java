package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.domain.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    class CreateSaleServiceTest {

        @Mock
        private SaleServiceInputPort saleService;

        @Mock
        private PaymentServiceInputPort paymentService;

        private CreateSaleService createSaleService;
        private UUID saleId;
        private Sale sale;

        @BeforeEach
        void setUp() {
            createSaleService = new CreateSaleService(saleService, paymentService);
            saleId = UUID.randomUUID();
            sale = new Sale();
            sale.setId(saleId);
        }

        @Test
        void shouldCreateSaleAndPaymentSuccessfully() {
            Sale createdSale = new Sale();
            createdSale.setId(saleId);

            when(saleService.create(sale)).thenReturn(createdSale);

            Sale result = createSaleService.createSale(sale);

            assertNotNull(result);
            assertEquals(saleId, result.getId());

            verify(saleService).create(sale);
            verify(paymentService).create(saleId);
        }

        @Test
        void shouldNotCreatePaymentWhenSaleCreationFails() {
            SaleUnprocessableEntityException exception =
                    new SaleUnprocessableEntityException("Error creating sale");

            when(saleService.create(sale)).thenThrow(exception);

            SaleUnprocessableEntityException thrown = assertThrows(
                    SaleUnprocessableEntityException.class,
                    () -> createSaleService.createSale(sale)
            );

            assertEquals("Error creating sale", thrown.getMessage());

            verify(saleService).create(sale);
            verifyNoInteractions(paymentService);
        }

        @Test
        void shouldPropagatePaymentNotFoundException() {
            Sale createdSale = new Sale();
            createdSale.setId(saleId);

            when(saleService.create(sale)).thenReturn(createdSale);
            when(paymentService.create(saleId))
                    .thenThrow(new PaymentNotFoundException("Payment not found"));

            PaymentNotFoundException exception = assertThrows(
                    PaymentNotFoundException.class,
                    () -> createSaleService.createSale(sale)
            );

            assertEquals("Payment not found", exception.getMessage());

            verify(saleService).create(sale);
            verify(paymentService).create(saleId);
        }
    }
