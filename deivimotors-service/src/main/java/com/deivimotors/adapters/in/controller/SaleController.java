package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.SaleMapper;
import com.deivimotors.adapters.in.controller.request.SaleRequest;
import com.deivimotors.adapters.in.controller.response.SaleResponse;
import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.ports.in.CreateSaleServiceInputPort;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.domain.Sale;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/sales")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleServiceInputPort saleService;
    private final CreateSaleServiceInputPort createSaleService;
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
    public ResponseEntity<SaleResponse> create (
            @Valid @RequestBody SaleRequest request
    ) throws SaleUnprocessableEntityException{
        Sale sale = mapper.toSale(request);
        Sale saleSave = createSaleService.createSale(sale);

        SaleResponse response = mapper.toSaleResponse(saleSave);

        URI location = URI.create("/sales/" + sale.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Buscar venda pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda localizada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> findById(@PathVariable UUID id) throws VehicleNotFoundException, SaleNotFoundException,  SaleUnprocessableEntityException {
        Sale sale = saleService.findById(id);
        SaleResponse response = mapper.toSaleResponse(sale);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Endpoint administrativo - Buscar vendas pelo CPF do client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendas localizadas"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping("/customer/{cpf}")
    public ResponseEntity<List<SaleResponse>> findByCustomerCpfAdmin(@PathVariable String cpf) throws VehicleNotFoundException, SaleNotFoundException,  SaleUnprocessableEntityException {
        List<Sale> sales = saleService.findByCustomerCpfAdmin(cpf);

        List<SaleResponse> response = sales.stream()
                .map(mapper::toSaleResponse)
                .toList();

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Buscar vendas pelo CPF do client logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendas localizadas"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping("/customer/purchases")
    public ResponseEntity<List<SaleResponse>> findByCustomerCpf() throws VehicleNotFoundException, SaleNotFoundException,  SaleUnprocessableEntityException {
        List<Sale> sales = saleService.findByCustomerCpfOrderByDateTimeSaleDesc();

        List<SaleResponse> response = sales.stream()
                .map(mapper::toSaleResponse)
                .toList();

        return ResponseEntity.ok().body(response);
    }



}
