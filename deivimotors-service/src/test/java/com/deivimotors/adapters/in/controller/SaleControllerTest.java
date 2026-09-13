package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.SaleMapper;
import com.deivimotors.adapters.in.controller.request.SaleRequest;
import com.deivimotors.adapters.in.controller.response.SaleResponse;
import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.in.CreateSaleServiceInputPort;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.domain.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @Mock
    private SaleServiceInputPort saleService;

    @Mock
    private CreateSaleServiceInputPort createSaleService;

    @Mock
    private SaleMapper mapper;

    private SaleController controller;
    private UUID saleId;
    private Sale sale;
    private SaleResponse saleResponse;

    @BeforeEach
    void setUp() {
        controller = new SaleController(saleService, createSaleService, mapper);

        saleId = UUID.randomUUID();
        sale = new Sale();
        sale.setId(saleId);

        saleResponse = mock(SaleResponse.class);
    }

    @Test
    void shouldCreateSaleSuccessfully() throws Exception {
        SaleRequest request = mock(SaleRequest.class);

        when(mapper.toSale(request)).thenReturn(sale);
        when(createSaleService.createSale(sale)).thenReturn(sale);
        when(mapper.toSaleResponse(sale)).thenReturn(saleResponse);

        ResponseEntity<SaleResponse> response = controller.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saleResponse, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/sales/" + saleId, response.getHeaders().getLocation().toString());

        verify(mapper).toSale(request);
        verify(createSaleService).createSale(sale);
        verify(mapper).toSaleResponse(sale);
    }

    @Test
    void shouldPropagateExceptionWhenCreatingSaleFails() throws Exception {
        SaleRequest request = mock(SaleRequest.class);

        when(mapper.toSale(request)).thenReturn(sale);
        when(createSaleService.createSale(sale)).thenThrow(new SaleUnprocessableEntityException("Error creating sale"));

        SaleUnprocessableEntityException exception = assertThrows(
                SaleUnprocessableEntityException.class,
                () -> controller.create(request)
        );

        assertEquals("Error creating sale", exception.getMessage());

        verify(mapper).toSale(request);
        verify(createSaleService).createSale(sale);
        verify(mapper, never()).toSaleResponse(any(Sale.class));
    }

    @Test
    void shouldFindSaleByIdSuccessfully() throws Exception {
        when(saleService.findById(saleId)).thenReturn(sale);
        when(mapper.toSaleResponse(sale)).thenReturn(saleResponse);

        ResponseEntity<SaleResponse> response = controller.findById(saleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saleResponse, response.getBody());

        verify(saleService).findById(saleId);
        verify(mapper).toSaleResponse(sale);
    }

    @Test
    void shouldPropagateExceptionWhenSaleIsNotFound() throws Exception {
        when(saleService.findById(saleId)).thenThrow(new SaleNotFoundException("Sale not found"));

        SaleNotFoundException exception = assertThrows(
                SaleNotFoundException.class,
                () -> controller.findById(saleId)
        );

        assertEquals("Sale not found", exception.getMessage());

        verify(saleService).findById(saleId);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldFindSalesByCustomerCpfAdminSuccessfully() throws Exception {
        String cpf = "12345678900";
        Sale sale2 = new Sale();
        sale2.setId(UUID.randomUUID());

        SaleResponse saleResponse2 = mock(SaleResponse.class);

        when(saleService.findByCustomerCpfAdmin(cpf)).thenReturn(List.of(sale, sale2));

        when(mapper.toSaleResponse(sale)).thenReturn(saleResponse);
        when(mapper.toSaleResponse(sale2)).thenReturn(saleResponse2);

        ResponseEntity<List<SaleResponse>> response = controller.findByCustomerCpfAdmin(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(saleResponse, response.getBody().get(0));
        assertEquals(saleResponse2, response.getBody().get(1));

        verify(saleService).findByCustomerCpfAdmin(cpf);
        verify(mapper).toSaleResponse(sale);
        verify(mapper).toSaleResponse(sale2);
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoSalesAdmin() throws Exception {
        String cpf = "12345678900";

        when(saleService.findByCustomerCpfAdmin(cpf)).thenReturn(List.of());

        ResponseEntity<List<SaleResponse>> response = controller.findByCustomerCpfAdmin(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(saleService).findByCustomerCpfAdmin(cpf);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldFindSalesByAuthenticatedCustomerSuccessfully() throws Exception {
        Sale sale2 = new Sale();
        sale2.setId(UUID.randomUUID());

        SaleResponse saleResponse2 = mock(SaleResponse.class);

        when(saleService.findByCustomerCpfOrderByDateTimeSaleDesc()).thenReturn(List.of(sale, sale2));

        when(mapper.toSaleResponse(sale)).thenReturn(saleResponse);
        when(mapper.toSaleResponse(sale2)).thenReturn(saleResponse2);

        ResponseEntity<List<SaleResponse>> response = controller.findByCustomerCpf();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(saleResponse, response.getBody().get(0));
        assertEquals(saleResponse2, response.getBody().get(1));

        verify(saleService).findByCustomerCpfOrderByDateTimeSaleDesc();
        verify(mapper).toSaleResponse(sale);
        verify(mapper).toSaleResponse(sale2);
    }

    @Test
    void shouldReturnEmptyListWhenAuthenticatedCustomerHasNoSales() throws Exception {
        when(saleService.findByCustomerCpfOrderByDateTimeSaleDesc()).thenReturn(List.of());

        ResponseEntity<List<SaleResponse>> response = controller.findByCustomerCpf();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(saleService).findByCustomerCpfOrderByDateTimeSaleDesc();
        verifyNoInteractions(mapper);
    }
}
