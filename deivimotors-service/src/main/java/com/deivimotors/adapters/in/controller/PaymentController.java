package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.PaymentMapper;
import com.deivimotors.adapters.in.controller.request.PaymentRequest;
import com.deivimotors.adapters.in.controller.response.PaymentResponse;
import com.deivimotors.application.exceptions.PaymentException;
import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.domain.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@AllArgsConstructor
@Validated
public class PaymentController {

    private final PaymentServiceInputPort service;

    private final PaymentMapper paymentMapper;

    @Operation(summary = "Buscar o status do pagamento pelo ID da venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento localizado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato errado do SaleId",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<PaymentResponse> findStatusPaymentOfSale(
            @PathVariable
            @NotNull
            UUID saleId
    ) throws PaymentNotFoundException{

        Payment payment = service.findPaymentBySaleId(saleId);
        var paymentResponse = paymentMapper.toPaymentResponse(payment);

        return ResponseEntity.ok().body(paymentResponse);
    }

    @Operation(summary = "Cadastrar um novo pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda não localizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato errado do SaleId",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/sale/{saleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable UUID saleId
    ) throws PaymentNotFoundException {
        var payment = service.create(saleId);
        var paymentResponse = paymentMapper.toPaymentResponse(payment);

        URI location = URI.create("/payments/sale/" + paymentResponse.saleId());
        return ResponseEntity.created(location).body(paymentResponse);
    }

    @Operation(summary = "Finalizar o checkout de um pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Checkout realizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venda ou Pagamento não localizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Venda não pode ser processado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato errado do SaleId",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/sale/{saleId}/checkout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> checkout(
            @PathVariable UUID saleId,
            @Valid @RequestBody  PaymentRequest request
    ) throws PaymentException {
        service.checkout(saleId, request.status());
        return ResponseEntity.noContent().build();
    }
}
