package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.List;

public record SalesOrder(
    String externalId,
    String customerExternalId,
    BigDecimal totalAmount,
    String currencyCode,
    List<String> lineDescriptions) {

  public SalesOrder {
    externalId = requireText(externalId, "externalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    totalAmount = Money.positive(totalAmount, "totalAmount");
    currencyCode = requireText(currencyCode, "currencyCode");
    lineDescriptions = List.copyOf(lineDescriptions);
    if (lineDescriptions.isEmpty()) {
      throw new IllegalArgumentException("lineDescriptions must not be empty");
    }
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
