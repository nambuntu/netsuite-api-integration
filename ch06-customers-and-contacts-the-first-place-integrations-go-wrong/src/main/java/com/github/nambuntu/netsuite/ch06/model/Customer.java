package com.github.nambuntu.netsuite.ch06.model;

public record Customer(
    String internalId,
    String externalId,
    String displayName,
    String email,
    String phone,
    boolean personCustomer,
    String sourceChannel) {

  public Customer {
    internalId = requireText(internalId, "internalId");
    externalId = requireText(externalId, "externalId");
    displayName = requireText(displayName, "displayName");
    email = requireText(email, "email");
    phone = requireText(phone, "phone");
    sourceChannel = requireText(sourceChannel, "sourceChannel");
  }

  public CustomerRef toRef() {
    return new CustomerRef(internalId, externalId);
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
