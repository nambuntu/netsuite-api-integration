package com.github.nambuntu.netsuite.ch09;

import java.util.List;
import java.util.Objects;

public record ItemFulfillment(
    String externalId,
    String salesOrderExternalId,
    FulfillmentStage stage,
    String shippingMethod,
    List<ItemFulfillmentLine> lines) {

  public ItemFulfillment {
    externalId = requireText(externalId, "externalId");
    salesOrderExternalId = requireText(salesOrderExternalId, "salesOrderExternalId");
    stage = Objects.requireNonNull(stage, "stage");
    shippingMethod = requireText(shippingMethod, "shippingMethod");
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
