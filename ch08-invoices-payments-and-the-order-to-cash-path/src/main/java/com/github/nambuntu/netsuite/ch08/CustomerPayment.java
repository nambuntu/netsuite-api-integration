package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.List;

public record CustomerPayment(
    String externalId,
    String customerExternalId,
    BigDecimal amount,
    String currencyCode,
    List<PaymentApplication> applications) {

  public CustomerPayment {
    externalId = requireText(externalId, "externalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    amount = Money.positive(amount, "amount");
    currencyCode = requireText(currencyCode, "currencyCode");
    applications = List.copyOf(applications);
    if (applications.isEmpty()) {
      throw new IllegalArgumentException("applications must not be empty");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
