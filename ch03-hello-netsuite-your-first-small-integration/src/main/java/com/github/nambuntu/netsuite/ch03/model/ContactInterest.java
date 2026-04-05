package com.github.nambuntu.netsuite.ch03.model;

import java.time.Instant;

public record ContactInterest(
    String externalId,
    String displayName,
    String email,
    String productCode,
    String campaignCode,
    Instant signupTimestamp,
    String syncStatus) {

  public ContactInterest {
    externalId = requireText(externalId, "externalId");
    displayName = requireText(displayName, "displayName");
    email = requireText(email, "email");
    productCode = requireText(productCode, "productCode");
    campaignCode = requireText(campaignCode, "campaignCode");
    syncStatus = requireText(syncStatus, "syncStatus");
    if (signupTimestamp == null) {
      throw new IllegalArgumentException("signupTimestamp must not be null");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
