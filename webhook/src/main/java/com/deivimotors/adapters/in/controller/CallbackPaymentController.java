package com.deivimotors.adapters.in.controller;

import com.deivimotors.adapters.in.controller.mapper.CallbackPaymentMapper;
import com.deivimotors.adapters.in.controller.request.CallbackPaymentRequest;
import com.deivimotors.domain.CallbackPayment;
import com.deivimotors.application.ports.in.CallbackPaymentInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Validated
public class CallbackPaymentController {

    private final CallbackPaymentInputPort callbackPaymentInputPort;
    private final CallbackPaymentMapper callbackPaymentMapper;

    @Operation(summary = "Recebe a resposta sobre o pagamento do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta do pagamento do pedido recebido com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))
            )
    })
    @PostMapping("/{id}")
    public ResponseEntity<Void> callBackPayment(
            @RequestBody(required = true) @Valid CallbackPaymentRequest request,
            @PathVariable UUID id){

        CallbackPayment callbackPayment = callbackPaymentMapper.toCallbackPayment(request);
        callbackPayment.setId(id);

        callbackPaymentInputPort.callbackPayment(callbackPayment);

        return ResponseEntity.ok().build();
    }

}
