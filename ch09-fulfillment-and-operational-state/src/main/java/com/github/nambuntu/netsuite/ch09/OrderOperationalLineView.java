package com.github.nambuntu.netsuite.ch09;

public record OrderOperationalLineView(
    String salesOrderLineExternalId,
    String itemName,
    int orderedQuantity,
    int fulfilledQuantity) {

  public OrderOperationalLineView {
    salesOrderLineExternalId = requireText(salesOrderLineExternalId, "salesOrderLineExternalId");
    itemName = requireText(itemName, "itemName");
    if (orderedQuantity <= 0) {
      throw new IllegalArgumentException("orderedQuantity must be greater than zero");
    }
    if (fulfilledQuantity < 0) {
      throw new IllegalArgumentException("fulfilledQuantity must not be negative");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
