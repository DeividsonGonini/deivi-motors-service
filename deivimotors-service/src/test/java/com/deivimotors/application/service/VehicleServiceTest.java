package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.exceptions.VehicleUnprocessableEntityException;
import com.deivimotors.application.ports.out.VehicleRepositoryOutputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepositoryOutputPort repository;

    private VehicleService vehicleService;

    private UUID vehicleId;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {

        vehicleService = new VehicleService(repository);

        vehicleId = UUID.randomUUID();

        vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setBrand("BMW");
        vehicle.setModel("X1");
        vehicle.setYear(2024);
        vehicle.setColor("Preto");
        vehicle.setPrice(new BigDecimal("150000.00"));
        vehicle.setStatus(VehicleStatusEnum.A_VENDA);
    }

    // CREATE
    @Test
    void shouldCreateVehicleSuccessfully() {

        UUID generatedId = UUID.randomUUID();

        when(repository.save(any(Vehicle.class)))
                .thenReturn(generatedId);

        UUID result = vehicleService.create(vehicle);

        assertNotNull(result);
        assertEquals(generatedId, result);
        assertNotNull(vehicle.getId());
        assertEquals(VehicleStatusEnum.A_VENDA, vehicle.getStatus());

        verify(repository).save(vehicle);
    }

    @Test
    void shouldSetVehicleStatusToAvailableWhenCreating() {

        vehicle.setStatus(VehicleStatusEnum.VENDIDO);

        UUID generatedId = UUID.randomUUID();

        when(repository.save(any(Vehicle.class))).thenReturn(generatedId);

        vehicleService.create(vehicle);

        assertEquals(VehicleStatusEnum.A_VENDA, vehicle.getStatus());

        verify(repository).save(vehicle);
    }

    // GET BY ID
    @Test
    void shouldFindVehicleByIdSuccessfully() {

        when(repository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getById(vehicleId);

        assertNotNull(result);

        assertEquals(vehicleId, result.getId());

        assertEquals("BMW", result.getBrand());

        assertEquals("X1", result.getModel());

        assertEquals(2024, result.getYear());

        assertEquals("Preto", result.getColor());

        assertEquals(new BigDecimal("150000.00"), result.getPrice());

        assertEquals(VehicleStatusEnum.A_VENDA, result.getStatus());

        verify(repository).findById(vehicleId);
    }

    @Test
    void shouldThrowExceptionWhenVehicleDoesNotExist() {

        when(repository.findById(vehicleId))
                .thenReturn(Optional.empty());

        VehicleNotFoundException exception =
                assertThrows(
                        VehicleNotFoundException.class,
                        () -> vehicleService.getById(vehicleId)
                );

        assertTrue(exception.getMessage().contains(vehicleId.toString()));

        verify(repository).findById(vehicleId);
    }

    // UPDATE
    @Test
    void shouldUpdateVehicleSuccessfully() {

        Vehicle vehicleToUpdate = new Vehicle();

        vehicleToUpdate.setBrand("Audi");
        vehicleToUpdate.setModel("Q5");
        vehicleToUpdate.setYear(2025);
        vehicleToUpdate.setColor("Branco");
        vehicleToUpdate.setPrice(new BigDecimal("180000.00"));
        vehicleToUpdate.setStatus(VehicleStatusEnum.A_VENDA);

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.update(vehicleId, vehicleToUpdate);

        assertNotNull(result);

        assertSame(vehicle, result);

        assertEquals(vehicleId, result.getId());

        assertEquals("Audi", result.getBrand());

        assertEquals("Q5", result.getModel());

        assertEquals(2025, result.getYear());

        assertEquals("Branco", result.getColor());

        assertEquals(new BigDecimal("180000.00"), result.getPrice());

        assertEquals(VehicleStatusEnum.A_VENDA, result.getStatus());

        verify(repository).findById(vehicleId);

        verify(repository).update(vehicle);
    }


    @Test
    void shouldUpdateOnlyProvidedVehicleFields() {

        Vehicle vehicleToUpdate = new Vehicle();

        vehicleToUpdate.setBrand("Toyota");
        vehicleToUpdate.setPrice(new BigDecimal("120000.00"));

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.update(vehicleId, vehicleToUpdate);

        assertNotNull(result);

        assertSame(vehicle, result);

        assertEquals("Toyota", result.getBrand());

        assertEquals("X1", result.getModel());

        assertEquals(2024, result.getYear());

        assertEquals("Preto", result.getColor());

        assertEquals(new BigDecimal("120000.00"), result.getPrice());

        assertEquals(VehicleStatusEnum.A_VENDA, result.getStatus());

        verify(repository).findById(vehicleId);

        verify(repository).update(vehicle);
    }

    @Test
    void shouldUpdateVehicleStatusWhenStatusIsProvided() {

        Vehicle vehicleToUpdate = new Vehicle();

        vehicleToUpdate.setStatus(VehicleStatusEnum.INATIVO);

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.update(vehicleId, vehicleToUpdate);

        assertNotNull(result);

        assertEquals(VehicleStatusEnum.INATIVO, result.getStatus());

        verify(repository).findById(vehicleId);

        verify(repository).update(vehicle);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingSoldVehicle() {

        vehicle.setStatus(VehicleStatusEnum.VENDIDO);

        Vehicle vehicleToUpdate = new Vehicle();

        vehicleToUpdate.setBrand("Audi");

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> vehicleService.update(vehicleId, vehicleToUpdate)
                );

        assertEquals("Vehicle sold, can't updated", exception.getMessage());

        verify(repository).findById(vehicleId);

        verify(repository, never()).update(any(Vehicle.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingVehicle() {

        Vehicle vehicleToUpdate = new Vehicle();

        vehicleToUpdate.setBrand("Audi");

        when(repository.findById(vehicleId)).thenReturn(Optional.empty());

        VehicleNotFoundException exception =
                assertThrows(VehicleNotFoundException.class,
                        () -> vehicleService.update(vehicleId, vehicleToUpdate)
                );

        assertTrue(exception.getMessage().contains(vehicleId.toString()));

        verify(repository).findById(vehicleId);

        verify(repository, never()).update(any(Vehicle.class));
    }

    // FIND BY STATUS
    @Test
    void shouldFindVehiclesByStatusOrderedByPrice() {

        Vehicle secondVehicle = new Vehicle();

        secondVehicle.setId(UUID.randomUUID());
        secondVehicle.setBrand("Audi");
        secondVehicle.setModel("Q5");
        secondVehicle.setPrice(new BigDecimal("180000.00")
        );
        secondVehicle.setStatus(VehicleStatusEnum.A_VENDA);

        List<Vehicle> vehicles = List.of(vehicle, secondVehicle);

        when(repository.findByStatusOrderByPriceAsc(VehicleStatusEnum.A_VENDA)).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);

        assertNotNull(result);

        assertEquals(2, result.size());

        assertEquals(vehicle, result.get(0));

        assertEquals(secondVehicle, result.get(1));

        verify(repository).findByStatusOrderByPriceAsc(VehicleStatusEnum.A_VENDA);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoVehiclesForStatus() {

        when(repository.findByStatusOrderByPriceAsc(VehicleStatusEnum.A_VENDA)).thenReturn(List.of());

        List<Vehicle> result = vehicleService.findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);

        assertNotNull(result);

        assertTrue(result.isEmpty());

        verify(repository).findByStatusOrderByPriceAsc(VehicleStatusEnum.A_VENDA);
    }


    // VALIDATION FOR SALE
    @Test
    void shouldAllowVehicleAvailableForSale() {
        assertDoesNotThrow(() -> vehicleService.validationVehicleForSale(VehicleStatusEnum.A_VENDA));
    }

    @Test
    void shouldNotAllowSoldVehicleForSale() {

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> vehicleService.validationVehicleForSale(
                                VehicleStatusEnum.VENDIDO
                        )
                );

        assertEquals("vehicle sold", exception.getMessage());
    }

    @Test
    void shouldNotAllowInactiveVehicleForSale() {

        PaymentUnprocessableEntityException exception =
                assertThrows(
                        PaymentUnprocessableEntityException.class,
                        () -> vehicleService.validationVehicleForSale(
                                VehicleStatusEnum.INATIVO
                        )
                );

        assertEquals("vehicle sold", exception.getMessage());
    }

    // UPDATE STATUS
    @Test
    void shouldUpdateVehicleToSold() {

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        vehicleService.updateStatus(vehicleId, VehicleStatusEnum.VENDIDO);

        assertEquals(VehicleStatusEnum.VENDIDO, vehicle.getStatus());

        verify(repository).findById(vehicleId);

        verify(repository).save(vehicle);
    }

    @Test
    void shouldUpdateVehicleToInactive() {

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        vehicleService.updateStatus(vehicleId, VehicleStatusEnum.INATIVO);

        assertEquals(VehicleStatusEnum.INATIVO, vehicle.getStatus());

        verify(repository).findById(vehicleId);

        verify(repository).save(vehicle);
    }

    @Test
    void shouldNotAllowUpdateToAvailable() {

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleUnprocessableEntityException exception =
                assertThrows(
                        VehicleUnprocessableEntityException.class,
                        () -> vehicleService.updateStatus(
                                vehicleId,
                                VehicleStatusEnum.A_VENDA
                        )
                );

        assertTrue(exception.getMessage().contains("Invalid status sequence"));

        verify(repository).findById(vehicleId);

        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void shouldNotAllowUpdateWhenVehicleIsAlreadySold() {

        vehicle.setStatus(VehicleStatusEnum.VENDIDO);

        when(repository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleUnprocessableEntityException exception =
                assertThrows(
                        VehicleUnprocessableEntityException.class,
                        () -> vehicleService.updateStatus(
                                vehicleId,
                                VehicleStatusEnum.INATIVO
                        )
                );

        assertTrue(exception.getMessage().contains("already been finalized"));

        verify(repository).findById(vehicleId);

        verify(repository, never()).save(any(Vehicle.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStatusOfNonExistingVehicle() {

        when(repository.findById(vehicleId)).thenReturn(Optional.empty());

        VehicleNotFoundException exception =
                assertThrows(
                        VehicleNotFoundException.class,
                        () -> vehicleService.updateStatus(
                                vehicleId,
                                VehicleStatusEnum.VENDIDO
                        )
                );

        assertTrue(exception.getMessage().contains(vehicleId.toString()));

        verify(repository).findById(vehicleId);

        verify(repository, never()).save(any(Vehicle.class));
    }
}
