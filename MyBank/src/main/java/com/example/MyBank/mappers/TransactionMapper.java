package com.example.MyBank.mappers;

import com.example.MyBank.entities.TransactionEntity;
import com.example.MyBank.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionEntity map(Transaction transaction);
}
