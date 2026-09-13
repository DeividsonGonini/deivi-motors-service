package com.deivimotors.application.service;


import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.config.security.AuthenticatedUser;
import com.deivimotors.config.security.AuthenticatedUserProvider;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.SaleStatusEnum;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepositoryOutputPort repository;

    @Mock
    private VehicleServiceInputPort vehicleService;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    private SaleService saleService;

    private UUID saleId;
    private UUID vehicleId;
    private String customerCpf;

    private Vehicle vehicle;
    private Sale sale;
    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {

        saleService = new SaleService(
                repository,
                vehicleService,
                authenticatedUserProvider
        );

        saleId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();
        customerCpf = "12345678900";

        vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setPrice(new BigDecimal("50000.00"));
        vehicle.setStatus(VehicleStatusEnum.A_VENDA);

        sale = new Sale();
        sale.setId(saleId);
        sale.setVehicle(vehicle);
        sale.setCustomerCpf(customerCpf);
        sale.setStatus(SaleStatusEnum.EM_ANDAMENTO);
        sale.setDateTimeSale(LocalDateTime.now());

        authenticatedUser = mock(AuthenticatedUser.class);
    }

    @Test
    void shouldCreateSaleSuccessfully() {

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        Sale result = saleService.create(sale);

        assertNotNull(result);
        assertNotNull(result.getId());

        assertEquals(
                SaleStatusEnum.EM_ANDAMENTO,
                result.getStatus()
        );

        assertNotNull(result.getDateTimeSale());

        assertEquals(
                customerCpf,
                result.getCustomerCpf()
        );

        assertEquals(
                vehicle,
                result.getVehicle()
        );

        assertEquals(
                vehicle.getPrice(),
                result.getTotalPrice()
        );

        verify(vehicleService)
                .getById(vehicleId);

        verify(vehicleService)
                .validationVehicleForSale(vehicle.getStatus());

        verify(repository)
                .save(sale);
    }

    @Test
    void shouldUseCustomerCpfFromAuthenticatedUser() {

        String requestCpf = "99999999999";

        sale.setCustomerCpf(requestCpf);

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        saleService.create(sale);

        assertEquals(
                customerCpf,
                sale.getCustomerCpf()
        );

        verify(repository)
                .save(sale);
    }

    @Test
    void shouldUseCpfFromRequestWhenThereIsNoAuthenticatedUser() {

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.empty());

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        Sale result = saleService.create(sale);

        assertEquals(
                customerCpf,
                result.getCustomerCpf()
        );

        verify(repository)
                .save(sale);
    }

    @Test
    void shouldThrowExceptionWhenCustomerCpfIsNull() {

        sale.setCustomerCpf(null);

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.empty());

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService.create(sale)
                );

        assertEquals(
                "Customer CPF was not provided",
                exception.getMessage()
        );

        verifyNoInteractions(
                repository,
                vehicleService
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerCpfIsBlank() {

        sale.setCustomerCpf("   ");

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.empty());

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService.create(sale)
                );

        assertEquals(
                "Customer CPF was not provided",
                exception.getMessage()
        );

        verifyNoInteractions(
                repository,
                vehicleService
        );
    }

    @Test
    void shouldSetVehiclePriceAsSaleTotalPrice() {

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        Sale result = saleService.create(sale);

        assertEquals(
                new BigDecimal("50000.00"),
                result.getTotalPrice()
        );

        verify(repository)
                .save(sale);
    }

    @Test
    void shouldFindSaleByIdSuccessfully() {

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        Sale result = saleService.findById(saleId);

        assertNotNull(result);

        assertEquals(
                saleId,
                result.getId()
        );

        assertEquals(
                customerCpf,
                result.getCustomerCpf()
        );

        assertEquals(
                vehicle,
                result.getVehicle()
        );

        verify(repository)
                .findById(saleId);

        verify(vehicleService)
                .getById(vehicleId);
    }

    @Test
    void shouldFindSaleWithoutAuthenticatedUser() {

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.empty());

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        Sale result = saleService.findById(saleId);

        assertNotNull(result);

        assertEquals(
                saleId,
                result.getId()
        );

        assertEquals(
                vehicle,
                result.getVehicle()
        );

        verify(repository)
                .findById(saleId);

        verify(vehicleService)
                .getById(vehicleId);
    }

    @Test
    void shouldThrowExceptionWhenSaleDoesNotExist() {

        when(repository.findById(saleId))
                .thenReturn(Optional.empty());

        SaleNotFoundException exception =
                assertThrows(
                        SaleNotFoundException.class,
                        () -> saleService.findById(saleId)
                );

        assertTrue(
                exception.getMessage().contains(
                        saleId.toString()
                )
        );

        verify(repository)
                .findById(saleId);

        verifyNoInteractions(
                vehicleService,
                authenticatedUserProvider
        );
    }

    @Test
    void shouldNotAllowSaleFromAnotherCustomer() {

        String anotherCpf = "99999999999";

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(anotherCpf);

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService.findById(saleId)
                );

        assertEquals(
                "The sale does not belong to the requested customer",
                exception.getMessage()
        );

        verify(repository)
                .findById(saleId);

        verifyNoInteractions(vehicleService);
    }

    @Test
    void shouldCompleteSaleSuccessfully() {

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        saleService.updateStatus(
                saleId,
                SaleStatusEnum.CONCLUIDO
        );

        assertEquals(
                SaleStatusEnum.CONCLUIDO,
                sale.getStatus()
        );

        verify(repository)
                .findById(saleId);

        verify(repository)
                .save(any(Sale.class));

        verify(vehicleService)
                .updateStatus(
                        vehicleId,
                        VehicleStatusEnum.VENDIDO
                );
    }

    @Test
    void shouldCancelSaleSuccessfully() {

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(repository.save(any(Sale.class)))
                .thenReturn(saleId);

        saleService.updateStatus(
                saleId,
                SaleStatusEnum.CANCELADO
        );

        assertEquals(
                SaleStatusEnum.CANCELADO,
                sale.getStatus()
        );

        verify(repository)
                .findById(saleId);

        verify(repository)
                .save(any(Sale.class));

        verify(vehicleService, never())
                .updateStatus(
                        any(UUID.class),
                        any(VehicleStatusEnum.class)
                );
    }

    @Test
    void shouldNotAllowUpdateToEmAndamento() {
        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        SaleUnprocessableEntityException exception =
                assertThrows
                        (SaleUnprocessableEntityException.class,
                                () -> saleService.updateStatus(saleId, SaleStatusEnum.EM_ANDAMENTO));

        assertTrue(exception.getMessage().contains("Invalid status sequence"));

        verify(repository).findById(saleId);
        verify(vehicleService).getById(vehicleId);
        verify(repository, never()).save(any(Sale.class));
        verify(vehicleService, never()).updateStatus(any(UUID.class), any(VehicleStatusEnum.class));
    }

    @Test
    void shouldNotAllowUpdateWhenSaleIsAlreadyCompleted() {

        sale.setStatus(SaleStatusEnum.CONCLUIDO);

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService.updateStatus(
                                saleId,
                                SaleStatusEnum.CANCELADO
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("already been finalized")
        );

        verify(repository)
                .findById(saleId);

        verify(vehicleService)
                .getById(vehicleId);

        verify(repository, never())
                .save(any(Sale.class));

        verify(vehicleService, never())
                .updateStatus(
                        any(UUID.class),
                        any(VehicleStatusEnum.class)
                );
    }

    @Test
    void shouldNotAllowUpdateWhenSaleIsAlreadyCanceled() {

        sale.setStatus(SaleStatusEnum.CANCELADO);

        when(repository.findById(saleId))
                .thenReturn(Optional.of(sale));

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService.updateStatus(
                                saleId,
                                SaleStatusEnum.CONCLUIDO
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("already been finalized")
        );

        verify(repository)
                .findById(saleId);

        verify(vehicleService)
                .getById(vehicleId);

        verify(repository, never())
                .save(any(Sale.class));

        verify(vehicleService, never())
                .updateStatus(
                        any(UUID.class),
                        any(VehicleStatusEnum.class)
                );
    }

    @Test
    void shouldFindSalesByAuthenticatedCustomerCpf() {

        Sale secondSale = new Sale();
        secondSale.setId(UUID.randomUUID());

        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setId(UUID.randomUUID());

        secondSale.setVehicle(secondVehicle);
        secondSale.setCustomerCpf(customerCpf);

        List<Sale> sales = List.of(
                sale,
                secondSale
        );

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.of(authenticatedUser));

        when(authenticatedUser.cpf())
                .thenReturn(customerCpf);

        when(repository.findByCustomerCpfOrderByDateTimeSaleDesc(
                customerCpf
        )).thenReturn(sales);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(vehicleService.getById(secondVehicle.getId()))
                .thenReturn(secondVehicle);

        List<Sale> result =
                saleService.findByCustomerCpfOrderByDateTimeSaleDesc();

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                vehicle,
                result.get(0).getVehicle()
        );

        assertEquals(
                secondVehicle,
                result.get(1).getVehicle()
        );

        verify(repository)
                .findByCustomerCpfOrderByDateTimeSaleDesc(
                        customerCpf
                );

        verify(vehicleService)
                .getById(vehicleId);

        verify(vehicleService)
                .getById(secondVehicle.getId());
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoAuthenticatedCustomer() {

        when(authenticatedUserProvider.getCurrentUser())
                .thenReturn(Optional.empty());

        SaleUnprocessableEntityException exception =
                assertThrows(
                        SaleUnprocessableEntityException.class,
                        () -> saleService
                                .findByCustomerCpfOrderByDateTimeSaleDesc()
                );

        assertEquals(
                "Authenticated customer not found",
                exception.getMessage()
        );

        verifyNoInteractions(repository);
        verifyNoInteractions(vehicleService);
    }

    @Test
    void shouldFindSalesByCustomerCpfForAdmin() {

        Sale secondSale = new Sale();
        secondSale.setId(UUID.randomUUID());

        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setId(UUID.randomUUID());

        secondSale.setVehicle(secondVehicle);
        secondSale.setCustomerCpf(customerCpf);

        List<Sale> sales = List.of(
                sale,
                secondSale
        );

        when(repository.findByCustomerCpfOrderByDateTimeSaleDesc(
                customerCpf
        )).thenReturn(sales);

        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        when(vehicleService.getById(secondVehicle.getId()))
                .thenReturn(secondVehicle);

        List<Sale> result =
                saleService.findByCustomerCpfAdmin(customerCpf);

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                vehicle,
                result.get(0).getVehicle()
        );

        assertEquals(
                secondVehicle,
                result.get(1).getVehicle()
        );

        verify(repository)
                .findByCustomerCpfOrderByDateTimeSaleDesc(
                        customerCpf
                );

        verify(vehicleService)
                .getById(vehicleId);

        verify(vehicleService)
                .getById(secondVehicle.getId());

        verifyNoInteractions(authenticatedUserProvider);
    }

    @Test
    void shouldReturnEmptyListWhenAdminQueryHasNoSales() {

        when(repository.findByCustomerCpfOrderByDateTimeSaleDesc(
                customerCpf
        )).thenReturn(List.of());

        List<Sale> result =
                saleService.findByCustomerCpfAdmin(customerCpf);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findByCustomerCpfOrderByDateTimeSaleDesc(
                        customerCpf
                );

        verifyNoInteractions(
                vehicleService,
                authenticatedUserProvider
        );
    }
}