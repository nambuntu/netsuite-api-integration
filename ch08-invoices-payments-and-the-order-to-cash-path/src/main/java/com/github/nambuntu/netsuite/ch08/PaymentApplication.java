package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;

public record PaymentApplication(String invoiceExternalId, BigDecimal appliedAmount) {

  public PaymentApplication {
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    appliedAmount = Money.positive(appliedAmount, "appliedAmount");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
