package com.github.nambuntu.netsuite.ch15.model;

import java.math.BigDecimal;

public record ReturnLine(
    String itemExternalId,
    int quantity,
    String originalSalesOrderLineReference,
    BigDecimal amount,
    ReturnReason reason) {
}
