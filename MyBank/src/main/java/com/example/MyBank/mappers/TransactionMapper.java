package com.example.MyBank.mappers;

import com.example.MyBank.entities.TransactionEntity;
import com.example.MyBank.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "type.enumeration", source = "type.enumeration")
    @Mapping(target = "type.value", source = "type.value")
    TransactionEntity map(Transaction transaction);
}
