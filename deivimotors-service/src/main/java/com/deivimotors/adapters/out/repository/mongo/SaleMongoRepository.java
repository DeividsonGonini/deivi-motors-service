package com.deivimotors.adapters.out.repository.mongo;

import com.deivimotors.adapters.out.repository.entity.SaleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface SaleMongoRepository extends MongoRepository<SaleEntity, UUID> {
    List<SaleEntity> findByCustomerCpfOrderByDateTimeSaleDesc(String customerCpf);
}
