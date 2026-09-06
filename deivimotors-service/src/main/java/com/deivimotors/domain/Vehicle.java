package com.deivimotors.domain;

import com.deivimotors.domain.enums.VehicleStatusEnum;

import java.math.BigDecimal;
import java.util.UUID;

public class Vehicle {

    private UUID id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private BigDecimal preco;
    private VehicleStatusEnum situacao;

    public Vehicle(UUID id,
                   String marca,
                   String modelo,
                   Integer ano,
                   String cor,
                   BigDecimal preco,
                   VehicleStatusEnum situacao
                   ) {

        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.preco = preco;
        this.situacao = situacao;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public VehicleStatusEnum getSituacao() {
        return situacao;
    }

    public void setSituacao(VehicleStatusEnum situacao) {
        this.situacao = situacao;
    }
}
