package com.github.nambuntu.netsuite.ch07;

public record Customer(String externalId, String displayName) {

  public Customer {
    externalId = requireText(externalId, "externalId");
    displayName = requireText(displayName, "displayName");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
