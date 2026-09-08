package com.deivimotors.adapters.out.repository.entity;

import com.deivimotors.domain.enums.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_payments")
public class PaymentEntity implements Serializable {

    @Id
    private UUID id;

    private UUID saleId;

    private PaymentStatusEnum status;
}

