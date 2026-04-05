package com.github.nambuntu.netsuite.ch06.model;

public record CustomerRef(String internalId, String externalId) {

  public CustomerRef {
    internalId = requireText(internalId, "internalId");
    externalId = requireText(externalId, "externalId");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
