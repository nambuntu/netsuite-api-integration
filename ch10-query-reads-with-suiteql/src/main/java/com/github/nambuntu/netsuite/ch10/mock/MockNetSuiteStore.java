package com.github.nambuntu.netsuite.ch10.mock;

import com.github.nambuntu.netsuite.ch10.model.ReconciliationState;
import java.math.BigDecimal;
import java.util.List;

public final class MockNetSuiteStore {

  public List<ReconciliationScenario> reconciliationScenarios() {
    return List.of(
        new ReconciliationScenario(
            "customer-theo-tran",
            "so-theo-1001",
            "inv-theo-1001",
            new BigDecimal("699.00"),
            new BigDecimal("300.00"),
            new BigDecimal("399.00"),
            "PARTIAL",
            "SHIPPED",
            ReconciliationState.OPEN_BALANCE),
        new ReconciliationScenario(
            "customer-theo-tran",
            "so-theo-1002",
            "inv-theo-1002",
            new BigDecimal("49.00"),
            new BigDecimal("49.00"),
            new BigDecimal("0.00"),
            "PAID",
            "PENDING",
            ReconciliationState.FULFILLMENT_LAG),
        new ReconciliationScenario(
            "customer-ava-cole",
            "so-ava-1003",
            "inv-ava-1003",
            new BigDecimal("149.00"),
            new BigDecimal("149.00"),
            new BigDecimal("0.00"),
            "PAID",
            "SHIPPED",
            ReconciliationState.SETTLED));
  }

  public List<ReconciliationScenario> attentionScenarios() {
    return reconciliationScenarios().stream()
        .filter(scenario -> scenario.reconciliationState() != ReconciliationState.SETTLED)
        .toList();
  }

  public record ReconciliationScenario(
      String customerExternalId,
      String salesOrderExternalId,
      String invoiceExternalId,
      BigDecimal invoiceTotal,
      BigDecimal amountPaid,
      BigDecimal amountDue,
      String paymentState,
      String fulfillmentState,
      ReconciliationState reconciliationState) {
  }
}
