package com.github.nambuntu.netsuite.ch06.model;

import java.util.Objects;

public record Contact(
    String internalId,
    String externalId,
    CustomerRef customer,
    String firstName,
    String lastName,
    String email,
    String phone,
    String role,
    boolean primaryContact,
    String sourceChannel) {

  public Contact {
    internalId = requireText(internalId, "internalId");
    externalId = requireText(externalId, "externalId");
    customer = Objects.requireNonNull(customer, "customer");
    firstName = requireText(firstName, "firstName");
    lastName = requireText(lastName, "lastName");
    email = requireText(email, "email");
    phone = requireText(phone, "phone");
    role = requireText(role, "role");
    sourceChannel = requireText(sourceChannel, "sourceChannel");
  }

  public ContactRef toRef() {
    return new ContactRef(internalId, externalId, customer.internalId());
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
