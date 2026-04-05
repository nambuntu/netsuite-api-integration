package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;

public record ApplyCustomerPaymentCommand(
    String paymentExternalId,
    String customerExternalId,
    String invoiceExternalId,
    BigDecimal amount,
    String currencyCode) {

  public ApplyCustomerPaymentCommand {
    paymentExternalId = requireText(paymentExternalId, "paymentExternalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    amount = Money.positive(amount, "amount");
    currencyCode = requireText(currencyCode, "currencyCode");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
