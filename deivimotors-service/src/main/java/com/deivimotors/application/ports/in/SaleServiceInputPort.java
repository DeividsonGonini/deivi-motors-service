package com.deivimotors.application.ports.in;

import com.deivimotors.domain.Sale;
import com.deivimotors.domain.enums.SaleStatusEnum;

import java.util.UUID;

public interface SaleServiceInputPort {
    Sale create(Sale sale);

    Sale findById(UUID saleId);


    void updateStatus(UUID saleId, SaleStatusEnum status);

    Sale update(UUID saleId, Sale sale);
}
