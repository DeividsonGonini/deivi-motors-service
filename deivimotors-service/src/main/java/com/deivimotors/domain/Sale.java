package com.deivimotors.domain;

import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.domain.enums.SaleStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class Sale {
    private UUID id;
    private Vehicle vehicle;
    private SaleStatusEnum status;
    private String client;
    private LocalDateTime dateTimeSale;

    public Sale(UUID id,
                Vehicle vehicle,
                SaleStatusEnum status,
                String client,
                LocalDateTime dateTimeSale) {
        this.id = id;
        this.vehicle = vehicle;
        this.status = status;
        this.client = client;
        this.dateTimeSale = dateTimeSale;
    }

//    public Sale(
//            UUID id,
//            SaleStatusEnum status
//    ){
//        this.id = id;
//        this.status = status;
//    }
//
//    public Sale() {
//    }

    public Sale completed() throws PaymentUnprocessableEntityException {
        if (this.status != SaleStatusEnum.EM_ANDAMENTO) {
            throw new PaymentUnprocessableEntityException("The current status: " + this.status.getStatus() +
                    " does not allow sale completed.");
        }
        this.status = SaleStatusEnum.CONCLUIDO;
        return this;
    }

    public Sale canceled() throws PaymentUnprocessableEntityException {
       this.status = SaleStatusEnum.CANCELADO;
       return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public SaleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SaleStatusEnum status) {
        this.status = status;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDateTime getDateTimeSale() {
        return dateTimeSale;
    }

    public void setDateTimeSale(LocalDateTime dateTimeSale) {
        this.dateTimeSale = dateTimeSale;
    }
}
