package com.github.nambuntu.netsuite.ch19.model;

import java.util.Objects;

public record LifecycleScenario(
    String campaignExternalId,
    String leadExternalId,
    String customerExternalId,
    String salesOrderExternalId,
    String invoiceExternalId,
    String paymentExternalId,
    String fulfillmentExternalId,
    String supportCaseExternalId,
    String returnAuthorizationExternalId,
    String replacementExternalId,
    String refundExternalId) {

  public LifecycleScenario {
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
    Objects.requireNonNull(salesOrderExternalId, "salesOrderExternalId");
    Objects.requireNonNull(invoiceExternalId, "invoiceExternalId");
    Objects.requireNonNull(paymentExternalId, "paymentExternalId");
    Objects.requireNonNull(fulfillmentExternalId, "fulfillmentExternalId");
    Objects.requireNonNull(supportCaseExternalId, "supportCaseExternalId");
    Objects.requireNonNull(returnAuthorizationExternalId, "returnAuthorizationExternalId");
    Objects.requireNonNull(replacementExternalId, "replacementExternalId");
    Objects.requireNonNull(refundExternalId, "refundExternalId");
  }

  public static LifecycleScenario theoScenario() {
    return new LifecycleScenario(
        "campaign-ai-pets-launch",
        "lead-theo-tran",
        "customer-theo-tran",
        "so-theo-1001",
        "inv-theo-1001",
        "payment-theo-1001",
        "fulfill-theo-1001",
        "case-theo-color-mismatch",
        "ra-theo-1001",
        "replacement-theo-1001",
        "refund-theo-1001");
  }
}
