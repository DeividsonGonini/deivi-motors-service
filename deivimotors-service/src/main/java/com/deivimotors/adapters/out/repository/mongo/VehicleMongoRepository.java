package com.deivimotors.adapters.out.repository.mongo;


import com.deivimotors.adapters.out.repository.entity.VehicleEntity;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;


public interface VehicleMongoRepository extends MongoRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findByStatusOrderByPriceAsc(VehicleStatusEnum situacao);
}
