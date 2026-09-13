package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.VehicleMapper;
import com.deivimotors.adapters.in.controller.request.VehicleRequest;
import com.deivimotors.adapters.in.controller.response.VehicleResponse;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleServiceInputPort service;

    @Mock
    private VehicleMapper mapper;

    private VehicleController controller;
    private UUID vehicleId;
    private Vehicle vehicle;
    private VehicleResponse vehicleResponse;

    @BeforeEach
    void setUp() {
        controller = new VehicleController(service, mapper);

        vehicleId = UUID.randomUUID();

        vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setBrand("BMW");
        vehicle.setModel("X1");
        vehicle.setYear(2024);
        vehicle.setColor("Preto");
        vehicle.setPrice(BigDecimal.valueOf(150600.00));
        vehicle.setStatus(VehicleStatusEnum.A_VENDA);

        vehicleResponse = mock(VehicleResponse.class);
    }

    @Test
    void shouldCreateVehicleSuccessfully() {
        VehicleRequest request = mock(VehicleRequest.class);

        when(mapper.toVehicle(request)).thenReturn(vehicle);
        when(mapper.toVehicleResponse(vehicle)).thenReturn(vehicleResponse);

        ResponseEntity<VehicleResponse> response = controller.createVehicle(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(vehicleResponse, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/vehicle/" + vehicleId, response.getHeaders().getLocation().toString());

        verify(mapper).toVehicle(request);
        verify(service).create(vehicle);
        verify(mapper).toVehicleResponse(vehicle);
    }

    @Test
    void shouldFindVehicleByIdSuccessfully() throws VehicleNotFoundException {
        when(service.getById(vehicleId)).thenReturn(vehicle);
        when(mapper.toVehicleResponse(vehicle)).thenReturn(vehicleResponse);

        ResponseEntity<VehicleResponse> response = controller.findById(vehicleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vehicleResponse, response.getBody());

        verify(service).getById(vehicleId);
        verify(mapper).toVehicleResponse(vehicle);
    }

    @Test
    void shouldPropagateExceptionWhenVehicleIsNotFound() throws VehicleNotFoundException {
        when(service.getById(vehicleId)).thenThrow(new VehicleNotFoundException("Vehicle not found"));

        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class,
                () -> controller.findById(vehicleId)
        );

        assertEquals("Vehicle not found", exception.getMessage());

        verify(service).getById(vehicleId);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldUpdateVehicleSuccessfully() {
        VehicleRequest request = mock(VehicleRequest.class);

        when(mapper.toVehicle(request)).thenReturn(vehicle);
        when(service.update(vehicleId, vehicle)).thenReturn(vehicle);
        when(mapper.toVehicleResponse(vehicle)).thenReturn(vehicleResponse);

        ResponseEntity<VehicleResponse> response = controller.update(vehicleId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vehicleResponse, response.getBody());

        verify(mapper).toVehicle(request);
        verify(service).update(vehicleId, vehicle);
        verify(mapper).toVehicleResponse(vehicle);
    }

    @Test
    void shouldFindVehiclesByStatusSuccessfully() {
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(UUID.randomUUID());

        VehicleResponse vehicleResponse2 = mock(VehicleResponse.class);

        when(service.findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA))
                .thenReturn(List.of(vehicle, vehicle2));

        when(mapper.toVehicleResponse(vehicle)).thenReturn(vehicleResponse);
        when(mapper.toVehicleResponse(vehicle2)).thenReturn(vehicleResponse2);

        ResponseEntity<List<VehicleResponse>> response =
                controller.findByStatusOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(vehicleResponse, response.getBody().get(0));
        assertEquals(vehicleResponse2, response.getBody().get(1));

        verify(service).findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);
        verify(mapper).toVehicleResponse(vehicle);
        verify(mapper).toVehicleResponse(vehicle2);
    }

    @Test
    void shouldReturnEmptyListWhenNoVehiclesAreFound() {
        when(service.findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA)).thenReturn(List.of());

        ResponseEntity<List<VehicleResponse>> response =
                controller.findByStatusOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(service).findBySituacaoOrderByPrecoAsc(VehicleStatusEnum.A_VENDA);
        verifyNoInteractions(mapper);
    }
}