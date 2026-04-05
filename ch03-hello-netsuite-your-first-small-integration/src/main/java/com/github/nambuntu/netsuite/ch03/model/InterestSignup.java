package com.github.nambuntu.netsuite.ch03.model;

import java.time.Instant;

public record InterestSignup(
    String externalId,
    String firstName,
    String lastName,
    String email,
    String productCode,
    String campaignCode,
    Instant signupTimestamp) {

  public InterestSignup {
    firstName = requireText(firstName, "firstName");
    lastName = requireText(lastName, "lastName");
    email = requireText(email, "email");
    productCode = requireText(productCode, "productCode");
    campaignCode = requireText(campaignCode, "campaignCode");
    if (signupTimestamp == null) {
      throw new IllegalArgumentException("signupTimestamp must not be null");
    }
    if (externalId != null) {
      externalId = externalId.trim();
      if (externalId.isEmpty()) {
        externalId = null;
      }
    }
  }

  public boolean hasExternalId() {
    return externalId != null && !externalId.isBlank();
  }

  public String displayName() {
    return firstName + " " + lastName;
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
