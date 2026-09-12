package com.deivimotors.application.ports.in;

import com.deivimotors.application.exceptions.SaleNotFoundException;
import com.deivimotors.application.exceptions.SaleUnprocessableEntityException;
import com.deivimotors.domain.Sale;
import com.deivimotors.domain.enums.SaleStatusEnum;

import java.util.UUID;

public interface SaleServiceInputPort {
    Sale create(Sale sale) throws SaleUnprocessableEntityException;

    Sale findById(UUID saleId) throws SaleNotFoundException, SaleUnprocessableEntityException;

    void updateStatus(UUID saleId, SaleStatusEnum status);

    Sale update(UUID saleId, Sale sale);
}
