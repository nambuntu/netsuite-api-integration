package com.github.nambuntu.netsuite.ch09;

public record SalesOrderLine(String externalId, String itemName, int orderedQuantity) {

  public SalesOrderLine {
    externalId = requireText(externalId, "externalId");
    itemName = requireText(itemName, "itemName");
    if (orderedQuantity <= 0) {
      throw new IllegalArgumentException("orderedQuantity must be greater than zero");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
