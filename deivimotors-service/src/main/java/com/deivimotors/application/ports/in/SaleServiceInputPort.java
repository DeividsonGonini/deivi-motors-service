package com.deivimotors.application.ports.in;

import com.deivimotors.domain.Sale;

import java.util.UUID;

public interface SaleServiceInputPort {
    Sale create(Sale sale);

    Sale getById(UUID saleId);

    Sale update(UUID saleId, Sale sale);
}
