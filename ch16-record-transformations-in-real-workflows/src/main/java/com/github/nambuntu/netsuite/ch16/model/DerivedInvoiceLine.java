package com.github.nambuntu.netsuite.ch16.model;

import java.math.BigDecimal;

public record DerivedInvoiceLine(
    String sourceSalesOrderLineReference,
    String itemExternalId,
    int quantity,
    BigDecimal amount) {
}
