package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.application.ports.in.CreateSaleServiceInputPort;
import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.application.ports.in.SaleServiceInputPort;
import com.deivimotors.domain.Sale;

public class CreateSaleService implements CreateSaleServiceInputPort {

    private final SaleServiceInputPort saleService;
    private final PaymentServiceInputPort paymentService;

    public CreateSaleService(
            SaleServiceInputPort saleService,
            PaymentServiceInputPort paymentService) {
        this.saleService = saleService;
        this.paymentService = paymentService;
    }


    @Override
    public Sale createSale(Sale sale) throws SaleUnprocessableEntityException, PaymentNotFoundException {

        Sale createdSale = saleService.create(sale);

        paymentService.create(createdSale.getId());

        return createdSale;
    }

}
