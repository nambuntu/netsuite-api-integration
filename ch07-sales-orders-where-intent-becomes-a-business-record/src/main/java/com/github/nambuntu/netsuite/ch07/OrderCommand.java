package com.github.nambuntu.netsuite.ch07;

import java.util.List;

public record OrderCommand(String orderExternalId, String customerExternalId, List<OrderLineCommand> lines) {

  public OrderCommand {
    orderExternalId = requireText(orderExternalId, "orderExternalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    lines = List.copyOf(lines);
    if (lines.isEmpty()) {
      throw new IllegalArgumentException("lines must not be empty");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
