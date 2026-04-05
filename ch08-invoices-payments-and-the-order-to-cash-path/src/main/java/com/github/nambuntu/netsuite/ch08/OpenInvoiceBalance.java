package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.Objects;

public record OpenInvoiceBalance(
    String invoiceExternalId,
    String customerExternalId,
    String createdFromSalesOrderExternalId,
    BigDecimal totalAmount,
    BigDecimal amountDue,
    String currencyCode,
    InvoiceStatus status) {

  public OpenInvoiceBalance {
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    createdFromSalesOrderExternalId = requireText(createdFromSalesOrderExternalId, "createdFromSalesOrderExternalId");
    totalAmount = Money.positive(totalAmount, "totalAmount");
    amountDue = Money.normalize(amountDue, "amountDue");
    currencyCode = requireText(currencyCode, "currencyCode");
    status = Objects.requireNonNull(status, "status");
  }

  public String summaryLine() {
    return "- " + invoiceExternalId
        + " | createdFrom=" + createdFromSalesOrderExternalId
        + " | amountDue=" + Money.format(amountDue, currencyCode)
        + " | status=" + status;
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
