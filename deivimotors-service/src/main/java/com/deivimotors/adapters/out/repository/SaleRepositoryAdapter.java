package com.deivimotors.adapters.out.repository;

import com.deivimotors.adapters.out.repository.entity.SaleEntity;
import com.deivimotors.adapters.out.repository.mapper.SaleEntityMapper;
import com.deivimotors.adapters.out.repository.mongo.SaleMongoRepository;
import com.deivimotors.application.ports.out.SaleRepositoryOutputPort;
import com.deivimotors.domain.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepositoryOutputPort {

    private final SaleMongoRepository mongoRepository;
    private final SaleEntityMapper mapper;

    @Override
    public UUID save(Sale sale) {
        SaleEntity saleEntity = mapper.toSaleEntity(sale);
        SaleEntity saleSave = mongoRepository.save(saleEntity);

        return saleSave.getId();
    }

    @Override
    public Optional<Sale> findById(UUID saleId) {
        return mongoRepository.findById(saleId)
                .map(mapper::toSale);
    }

    @Override
    public Sale update(Sale sale) {
        return null;
    }
}
