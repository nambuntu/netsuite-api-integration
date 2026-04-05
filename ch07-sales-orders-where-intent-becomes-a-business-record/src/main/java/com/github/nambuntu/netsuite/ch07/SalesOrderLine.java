package com.github.nambuntu.netsuite.ch07;

public record SalesOrderLine(int lineNumber, String itemExternalId, String itemDisplayName, int quantity) {

  public SalesOrderLine {
    if (lineNumber <= 0) {
      throw new IllegalArgumentException("lineNumber must be greater than zero");
    }
    itemExternalId = requireText(itemExternalId, "itemExternalId");
    itemDisplayName = requireText(itemDisplayName, "itemDisplayName");
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be greater than zero");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
