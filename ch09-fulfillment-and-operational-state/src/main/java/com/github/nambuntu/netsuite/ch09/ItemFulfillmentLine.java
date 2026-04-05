package com.github.nambuntu.netsuite.ch09;

public record ItemFulfillmentLine(String salesOrderLineExternalId, int fulfilledQuantity) {

  public ItemFulfillmentLine {
    salesOrderLineExternalId = requireText(salesOrderLineExternalId, "salesOrderLineExternalId");
    if (fulfilledQuantity <= 0) {
      throw new IllegalArgumentException("fulfilledQuantity must be greater than zero");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
