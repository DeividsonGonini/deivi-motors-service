package com.deivimotors.adapters.out.repository.entity;

import com.deivimotors.domain.enums.VehicleStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_vehicles")
public class VehicleEntity implements Serializable {

    @Id
    private UUID id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal preco;
    private VehicleStatusEnum situacao;

}
