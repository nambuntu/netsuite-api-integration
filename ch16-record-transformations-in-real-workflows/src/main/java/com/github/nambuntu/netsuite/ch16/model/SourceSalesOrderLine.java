package com.github.nambuntu.netsuite.ch16.model;

import java.math.BigDecimal;

public record SourceSalesOrderLine(
    String externalId,
    String itemExternalId,
    int quantity,
    BigDecimal amount) {
}
