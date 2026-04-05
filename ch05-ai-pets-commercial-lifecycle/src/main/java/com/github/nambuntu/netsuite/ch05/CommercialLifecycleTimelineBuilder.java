package com.github.nambuntu.netsuite.ch05;

public class CommercialLifecycleTimelineBuilder {

  public LifecycleTimeline buildTheoLifecycleTimeline() {
    CommercialLifecycleIds ids = CommercialLifecycleIds.theoNimbusCatStory();
    LifecycleTimeline timeline = new LifecycleTimeline();

    timeline.record(LifecyclePhase.CAMPAIGN, ids.campaignId(),
        "Ava launches the Nimbus Cat campaign");
    timeline.record(LifecyclePhase.INTEREST_CAPTURED, ids.leadId(),
        "Theo responds and enters CRM tracking");
    timeline.record(LifecyclePhase.CUSTOMER_HANDOFF, ids.customerId(),
        "Theo becomes a customer with preserved source attribution");
    timeline.record(LifecyclePhase.SALES_ORDER, ids.salesOrderId(),
        "Theo places a sales order");
    timeline.record(LifecyclePhase.INVOICE, ids.salesOrderId(),
        "The order becomes billable");
    timeline.record(LifecyclePhase.PAYMENT, ids.salesOrderId(),
        "Payment is received against the same order story");
    timeline.record(LifecyclePhase.FULFILLMENT, ids.salesOrderId(),
        "Jules ships the order");
    timeline.record(LifecyclePhase.SUPPORT_CASE, ids.supportCaseId(),
        "Mira opens a support case about the wrong colour");
    timeline.record(LifecyclePhase.RETURN_AUTHORIZATION, ids.returnAuthorizationId(),
        "The business authorizes a return");
    timeline.record(LifecyclePhase.REFUND, ids.refundId(),
        "The money side is reversed");

    return timeline;
  }
}
