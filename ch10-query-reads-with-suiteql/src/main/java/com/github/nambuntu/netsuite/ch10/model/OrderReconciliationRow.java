package com.github.nambuntu.netsuite.ch10.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record OrderReconciliationRow(
    String customerExternalId,
    String salesOrderExternalId,
    String invoiceExternalId,
    BigDecimal invoiceTotal,
    BigDecimal amountPaid,
    BigDecimal amountDue,
    String paymentState,
    String fulfillmentState,
    ReconciliationState reconciliationState) {

  public OrderReconciliationRow {
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    salesOrderExternalId = requireText(salesOrderExternalId, "salesOrderExternalId");
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    invoiceTotal = normalize(invoiceTotal, "invoiceTotal");
    amountPaid = normalize(amountPaid, "amountPaid");
    amountDue = normalize(amountDue, "amountDue");
    paymentState = requireText(paymentState, "paymentState");
    fulfillmentState = requireText(fulfillmentState, "fulfillmentState");
  }

  public OrderReconciliationRow withReconciliationState(ReconciliationState state) {
    return new OrderReconciliationRow(
        customerExternalId,
        salesOrderExternalId,
        invoiceExternalId,
        invoiceTotal,
        amountPaid,
        amountDue,
        paymentState,
        fulfillmentState,
        Objects.requireNonNull(state, "state"));
  }

  public boolean needsAttention() {
    return reconciliationState != ReconciliationState.SETTLED;
  }

  private static BigDecimal normalize(BigDecimal value, String fieldName) {
    if (value == null) {
      throw new IllegalArgumentException(fieldName + " must not be null");
    }
    return value.setScale(2, RoundingMode.UNNECESSARY);
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
