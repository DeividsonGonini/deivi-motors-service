package com.deivimotors.adapters.out.repository;

import com.deivimotors.adapters.out.repository.entity.VehicleEntity;
import com.deivimotors.adapters.out.repository.mapper.VehicleEntityMapper;
import com.deivimotors.adapters.out.repository.mongo.VehicleMongoRepository;
import com.deivimotors.application.ports.out.VehicleRepositoryOutputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.domain.enums.VehicleStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehicleRepositoryAdapter implements VehicleRepositoryOutputPort {

    private final VehicleMongoRepository mongoRepository;
    private final VehicleEntityMapper mapper;

    @Override
    public UUID save(Vehicle vehicle) {
        VehicleEntity vehicleEntity = mapper.toVehicleEntity(vehicle);
        VehicleEntity vehicleSave = mongoRepository.save(vehicleEntity);

        return vehicleSave.getId();
    }

    @Override
    public Optional<Vehicle> findById(UUID idVehicle) {
        return mongoRepository.findById(idVehicle)
                .map(mapper::toVehicle);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        VehicleEntity vehicleEntity = mapper.toVehicleEntity(vehicle);
        var savedVehicle = mongoRepository.save(vehicleEntity);
        return mapper.toVehicle(savedVehicle);
    }

    public List<Vehicle> findByStatusOrderByPriceAsc(VehicleStatusEnum status) {

        List<VehicleEntity> vehicleEntities = mongoRepository.findByStatusOrderByPriceAsc(status);

        return vehicleEntities.stream()
                .map(mapper::toVehicle)
                .toList();
    }

}
