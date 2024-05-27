package com.example.MyBank.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
    GBS_TRANSACTION_TYPE_0023("GBS_TRANSACTION_TYPE_0023"),
    GBS_TRANSACTION_TYPE_0015("GBS_TRANSACTION_TYPE_0015"),
    UNKNOWN("UNKNOWN");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TransactionType fromValue(String value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
