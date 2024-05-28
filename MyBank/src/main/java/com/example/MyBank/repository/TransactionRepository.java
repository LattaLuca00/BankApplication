package com.example.MyBank.repository;

import com.example.MyBank.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,String> {
}
