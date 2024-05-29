package com.example.MyBank.entities;

import com.example.MyBank.model.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "accounting_date")
    private Date accountingDate;

    @Column(name = "value_date")
    private Date valueDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enumeration", column = @Column(name = "type_enumeration")),
            @AttributeOverride(name = "value", column = @Column(name = "type_value"))
    })
    private TransactionType type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

}
