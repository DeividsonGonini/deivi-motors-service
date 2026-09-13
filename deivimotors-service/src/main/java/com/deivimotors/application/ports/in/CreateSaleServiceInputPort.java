package com.deivimotors.application.ports.in;

import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.domain.Sale;

public interface CreateSaleServiceInputPort {

    Sale createSale(Sale sale) throws SaleUnprocessableEntityException, PaymentNotFoundException;

}
