package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.Objects;

public record Invoice(
    String externalId,
    String customerExternalId,
    String createdFromSalesOrderExternalId,
    BigDecimal totalAmount,
    BigDecimal amountDue,
    String currencyCode,
    InvoiceStatus status) {

  public Invoice {
    externalId = requireText(externalId, "externalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    createdFromSalesOrderExternalId = requireText(createdFromSalesOrderExternalId, "createdFromSalesOrderExternalId");
    totalAmount = Money.positive(totalAmount, "totalAmount");
    amountDue = Money.normalize(amountDue, "amountDue");
    currencyCode = requireText(currencyCode, "currencyCode");
    status = Objects.requireNonNull(status, "status");
    if (amountDue.compareTo(totalAmount) > 0) {
      throw new IllegalArgumentException("amountDue must not exceed totalAmount");
    }
  }

  public Invoice applyPayment(BigDecimal appliedAmount) {
    BigDecimal normalizedAppliedAmount = Money.positive(appliedAmount, "appliedAmount");
    if (normalizedAppliedAmount.compareTo(amountDue) > 0) {
      throw new IllegalArgumentException("payment would over-apply invoice " + externalId);
    }
    BigDecimal newAmountDue = amountDue.subtract(normalizedAppliedAmount);
    InvoiceStatus newStatus = newAmountDue.signum() == 0 ? InvoiceStatus.PAID : InvoiceStatus.PARTIALLY_PAID;
    return new Invoice(
        externalId,
        customerExternalId,
        createdFromSalesOrderExternalId,
        totalAmount,
        newAmountDue,
        currencyCode,
        newStatus);
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
