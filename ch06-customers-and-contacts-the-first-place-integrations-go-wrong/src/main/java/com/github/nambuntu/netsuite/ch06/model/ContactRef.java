package com.github.nambuntu.netsuite.ch06.model;

public record ContactRef(String internalId, String externalId, String customerInternalId) {

  public ContactRef {
    internalId = requireText(internalId, "internalId");
    externalId = requireText(externalId, "externalId");
    customerInternalId = requireText(customerInternalId, "customerInternalId");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
