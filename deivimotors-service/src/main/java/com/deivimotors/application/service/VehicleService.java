package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.VehicleNotFoundException;
import com.deivimotors.application.ports.out.VehicleRepositoryOutputPort;
import com.deivimotors.domain.Vehicle;
import com.deivimotors.application.ports.in.VehicleServiceInputPort;
import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleService implements VehicleServiceInputPort {
    private VehicleRepositoryOutputPort repository;

    public VehicleService(VehicleRepositoryOutputPort repository) {
        this.repository = repository;
    }

    @Override
    public String create(Vehicle vehicle) {

        String id = UUID.randomUUID().toString();
        vehicle.setId(id);
        vehicle.setSituacao(VehicleStatusEnum.A_VENDA);

        String idVehicle = repository.save(vehicle);
        return idVehicle;
    }

    @Override
    public Vehicle getById(String vehicleId) throws VehicleNotFoundException {
        Optional<Vehicle> vehicle = Optional.ofNullable(repository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: " + vehicleId)));

        return vehicle.get();
    }

    @Override
    public Vehicle update(String vehicleId, Vehicle vehicle) throws VehicleNotFoundException{
        Vehicle currentVehicle = findById(vehicleId);

        Vehicle updateVehicle = updateFrom(currentVehicle, vehicle);

        repository.update(updateVehicle);

        return null;
    }

    @Override
    public List<Vehicle> findBySituacaoOrderByPrecoAsc(VehicleStatusEnum status) {
        return repository.findBySituacaoOrderByPrecoAsc(status);
    }

    private Vehicle findById(String vehicleId) throws VehicleNotFoundException {
        Optional<Vehicle> vehicle = Optional.ofNullable(repository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: : " + vehicleId)));
        return vehicle.get();
    }

    private Vehicle updateFrom(Vehicle currentVehicle, Vehicle vehicleToUpdate){
        if(vehicleToUpdate.getMarca() != null) currentVehicle.setMarca(vehicleToUpdate.getMarca());
        if(vehicleToUpdate.getModelo() != null) currentVehicle.setModelo(vehicleToUpdate.getModelo());
        if(vehicleToUpdate.getAno() != null) currentVehicle.setAno(vehicleToUpdate.getAno());
        if(vehicleToUpdate.getCor() != null) currentVehicle.setCor(vehicleToUpdate.getCor());
        if(vehicleToUpdate.getPreco() != null) currentVehicle.setPreco(vehicleToUpdate.getPreco());
        if(vehicleToUpdate.getSituacao() != null) currentVehicle.setSituacao(vehicleToUpdate.getSituacao());
        return  currentVehicle;
    }
}
