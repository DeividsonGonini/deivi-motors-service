package com.deivimotors.application.ports.out;

import com.deivimotors.domain.Sale;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepositoryOutputPort {

    UUID save(Sale sale);

    Optional<Sale> findById(UUID idSale);

    Sale update (Sale sale);

    List<Sale> findByCustomerCpfOrderByDateTimeSaleDesc(String custormerCpf);
}
