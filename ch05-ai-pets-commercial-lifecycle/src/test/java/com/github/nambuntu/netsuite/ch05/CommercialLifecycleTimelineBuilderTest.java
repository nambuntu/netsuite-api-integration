package com.github.nambuntu.netsuite.ch05;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommercialLifecycleTimelineBuilderTest {

  private final CommercialLifecycleTimelineBuilder builder = new CommercialLifecycleTimelineBuilder();

  @Test
  void buildTheoLifecycleTimelineIncludesAllExpectedPhasesInOrderWithCanonicalIds() {
    LifecycleTimeline timeline = builder.buildTheoLifecycleTimeline();

    assertThat(timeline.events()).extracting(LifecycleEvent::phase).containsExactly(
        LifecyclePhase.CAMPAIGN,
        LifecyclePhase.INTEREST_CAPTURED,
        LifecyclePhase.CUSTOMER_HANDOFF,
        LifecyclePhase.SALES_ORDER,
        LifecyclePhase.INVOICE,
        LifecyclePhase.PAYMENT,
        LifecyclePhase.FULFILLMENT,
        LifecyclePhase.SUPPORT_CASE,
        LifecyclePhase.RETURN_AUTHORIZATION,
        LifecyclePhase.REFUND);

    assertThat(timeline.events()).extracting(LifecycleEvent::primaryExternalId).containsExactly(
        "campaign-ai-pets-launch",
        "lead-theo-tran",
        "customer-theo-tran",
        "so-theo-1001",
        "so-theo-1001",
        "so-theo-1001",
        "so-theo-1001",
        "case-theo-color-mismatch",
        "ra-theo-1001",
        "refund-theo-1001");
  }

  @Test
  void orderInvoicePaymentAndFulfillmentReuseTheSalesOrderId() {
    LifecycleTimeline timeline = builder.buildTheoLifecycleTimeline();

    List<LifecycleEvent> commerceCore = timeline.events().stream()
        .filter(event -> event.phase() == LifecyclePhase.SALES_ORDER
            || event.phase() == LifecyclePhase.INVOICE
            || event.phase() == LifecyclePhase.PAYMENT
            || event.phase() == LifecyclePhase.FULFILLMENT)
        .toList();

    assertThat(commerceCore).hasSize(4);
    assertThat(commerceCore).extracting(LifecycleEvent::primaryExternalId)
        .containsOnly("so-theo-1001");
  }

  @Test
  void supportReturnAndRefundAppearAfterFulfillmentInTheExpectedOrder() {
    LifecycleTimeline timeline = builder.buildTheoLifecycleTimeline();

    assertThat(timeline.events().subList(6, 10)).extracting(LifecycleEvent::phase).containsExactly(
        LifecyclePhase.FULFILLMENT,
        LifecyclePhase.SUPPORT_CASE,
        LifecyclePhase.RETURN_AUTHORIZATION,
        LifecyclePhase.REFUND);
  }
}
