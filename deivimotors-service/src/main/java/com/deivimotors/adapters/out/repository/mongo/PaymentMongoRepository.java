package com.deivimotors.adapters.out.repository.mongo;

import com.deivimotors.adapters.out.repository.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentMongoRepository extends MongoRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findBySaleId(UUID saleId);
}
