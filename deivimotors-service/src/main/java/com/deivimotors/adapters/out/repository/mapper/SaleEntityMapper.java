package com.deivimotors.adapters.out.repository.mapper;

import com.deivimotors.adapters.out.repository.entity.SaleEntity;
import com.deivimotors.domain.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleEntityMapper {

    Sale toSale(SaleEntity saleEntity);

    SaleEntity toSaleEntity(Sale sale);
}
