package com.deivimotors.domain;

import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.application.exceptions.VehicleUnprocessableEntityException;
import com.deivimotors.domain.enums.SaleStatusEnum;
import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.math.BigDecimal;
import java.util.UUID;

public class Vehicle {

    private UUID id;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private BigDecimal price;
    private VehicleStatusEnum status;

    public Vehicle(UUID id,
                   String brand,
                   String model,
                   Integer year,
                   String color,
                   BigDecimal price,
                   VehicleStatusEnum status
                   ) {

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = status;
    }

    public static Vehicle fromId(UUID id) {
        return new Vehicle(
                id,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Vehicle sold() throws VehicleUnprocessableEntityException {
        if (this.status != VehicleStatusEnum.A_VENDA) {
            throw new VehicleUnprocessableEntityException("The current status: " + this.status.getStatus() +
                    " does not allow sale completed of vehicle.");
        }
        this.status = VehicleStatusEnum.VENDIDO;
        return this;
    }

    public Vehicle inactive() {
        this.status = VehicleStatusEnum.INATIVO;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public VehicleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(VehicleStatusEnum status) {
        this.status = status;
    }

}
