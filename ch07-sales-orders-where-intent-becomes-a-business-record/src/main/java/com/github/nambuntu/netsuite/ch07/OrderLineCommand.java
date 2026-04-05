package com.github.nambuntu.netsuite.ch07;

public record OrderLineCommand(String itemExternalId, int quantity) {

  public OrderLineCommand {
    itemExternalId = requireText(itemExternalId, "itemExternalId");
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
