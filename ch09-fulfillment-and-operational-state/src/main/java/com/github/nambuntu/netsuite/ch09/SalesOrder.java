package com.github.nambuntu.netsuite.ch09;

import java.util.List;
import java.util.Objects;

public record SalesOrder(
    String externalId,
    String customerExternalId,
    InvoiceContext invoiceContext,
    List<SalesOrderLine> lines) {

  public SalesOrder {
    externalId = requireText(externalId, "externalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    invoiceContext = Objects.requireNonNull(invoiceContext, "invoiceContext");
    lines = List.copyOf(lines);
    if (lines.isEmpty()) {
      throw new IllegalArgumentException("lines must not be empty");
    }
  }

  public SalesOrderLine requireLine(String externalId) {
    return lines.stream()
        .filter(line -> line.externalId().equals(externalId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("unknown sales order line external ID: " + externalId));
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
