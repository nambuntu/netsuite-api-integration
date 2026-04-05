package com.github.nambuntu.netsuite.ch05;

public record CommercialLifecycleIds(
    String campaignId,
    String leadId,
    String customerId,
    String salesOrderId,
    String supportCaseId,
    String returnAuthorizationId,
    String refundId) {

  public CommercialLifecycleIds {
    campaignId = requireText(campaignId, "campaignId");
    leadId = requireText(leadId, "leadId");
    customerId = requireText(customerId, "customerId");
    salesOrderId = requireText(salesOrderId, "salesOrderId");
    supportCaseId = requireText(supportCaseId, "supportCaseId");
    returnAuthorizationId = requireText(returnAuthorizationId, "returnAuthorizationId");
    refundId = requireText(refundId, "refundId");
  }

  public static CommercialLifecycleIds theoNimbusCatStory() {
    return new CommercialLifecycleIds(
        "campaign-ai-pets-launch",
        "lead-theo-tran",
        "customer-theo-tran",
        "so-theo-1001",
        "case-theo-color-mismatch",
        "ra-theo-1001",
        "refund-theo-1001");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
