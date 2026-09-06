package com.deivimotors.adapters.out.repository.entity;


import org.springframework.data.annotation.Id;
import com.deivimotors.domain.enums.SaleStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_sales")
public class SaleEntity {

    @Id
    private UUID id;

    //TODO ajustar pra salvar o ID Veiculo
    private VehicleEntity vehicle;
    private SaleStatusEnum status;
    private String client;
    private LocalDateTime dateTimeSale;
}
