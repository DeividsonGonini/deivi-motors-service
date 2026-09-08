package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.VehicleMapper;
import com.deivimotors.adapters.in.controller.request.VehicleRequest;
import com.deivimotors.adapters.in.controller.response.VehicleResponse;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/vehicles")
@AllArgsConstructor
@Validated
public class VehicleController {

    private final VehicleServiceInputPort service;
    private final VehicleMapper mapper;

    @Operation(summary = "Cadastrar um novo veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "veículo cadstrado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao cadastrar Veículo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VehicleResponse> createVehicle(
            @Valid @RequestBody VehicleRequest request
    ){
        System.out.println("REQUEST = " + request);
        var vehicle = mapper.toVehicle(request);

        service.create(vehicle);
        VehicleResponse response = mapper.toVehicleResponse(vehicle);

        URI location = URI.create("/vehicle/" + vehicle.getId());
        return ResponseEntity.created(location).body(response);
    }


    @Operation(summary = "Buscar veículo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo localizado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findById(@PathVariable UUID id) throws VehicleNotFoundException{
        Vehicle vehicle = service.getById(id);
        VehicleResponse response = mapper.toVehicleResponse(vehicle);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "Atualizar veículo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody VehicleRequest request
    ){
        Vehicle vehicle = mapper.toVehicle(request);
        Vehicle updatedVehicle = service.update(id, vehicle);

        VehicleResponse response = mapper.toVehicleResponse(updatedVehicle);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "Lista de veículo pelo por Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículos localizados com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Situação inválida para busca",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> findByStatusOrderByPrecoAsc(
            @RequestParam VehicleStatusEnum status
            ){
        List<Vehicle> vehicles = service.findBySituacaoOrderByPrecoAsc(status);

        List<VehicleResponse> response = vehicles.stream()
                .map(mapper::toVehicleResponse)
                .toList();
        return ResponseEntity.ok().body(response);
    }

}
