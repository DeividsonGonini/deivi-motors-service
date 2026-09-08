package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.SaleMapper;
import com.deivimotors.adapters.in.controller.request.SaleRequest;
import com.deivimotors.adapters.in.controller.response.SaleResponse;
import com.deivimotors.adapters.in.controller.response.VehicleResponse;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.Vehicle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/sales")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleServiceInputPort service;
    private final SaleMapper mapper;

    @Operation(summary = "Cadastrar uma nova venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda cadastrada com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping
    public ResponseEntity<SaleResponse> create(
            @Valid @RequestBody SaleRequest request
    ){
        Sale sale = mapper.toSale(request);
        Sale saleSave = service.create(sale);

        SaleResponse response = mapper.toSaleResponse(saleSave);

        URI location = URI.create("/sales/" + sale.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Buscar venda pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda localizado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> findById(@PathVariable UUID id) throws VehicleNotFoundException {
        Sale sale = service.findById(id);
        SaleResponse response = mapper.toSaleResponse(sale);
        return ResponseEntity.ok().body(response);
    }
}
