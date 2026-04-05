package com.github.nambuntu.netsuite.ch07;

import java.util.List;
import java.util.Objects;

public record SalesOrder(String externalId, Customer customer, List<SalesOrderLine> lines, OrderStatus status) {

  public SalesOrder {
    externalId = requireText(externalId, "externalId");
    customer = Objects.requireNonNull(customer, "customer");
    lines = List.copyOf(lines);
    if (lines.isEmpty()) {
      throw new IllegalArgumentException("lines must not be empty");
    }
    status = Objects.requireNonNull(status, "status");
  }

  public int totalLines() {
    return lines.size();
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
