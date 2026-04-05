package com.github.nambuntu.netsuite.ch05;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LifecycleTimelineTest {

  @Test
  void recordingTheSamePhaseAndPrimaryExternalIdTwiceDoesNotDuplicateTheTimeline() {
    LifecycleTimeline timeline = new LifecycleTimeline();

    boolean first = timeline.record(LifecyclePhase.SALES_ORDER, "so-theo-1001", "Theo places a sales order");
    boolean second = timeline.record(LifecyclePhase.SALES_ORDER, "so-theo-1001", "Theo places a sales order again");

    assertThat(first).isTrue();
    assertThat(second).isFalse();
    assertThat(timeline.events()).singleElement()
        .extracting(LifecycleEvent::primaryExternalId)
        .isEqualTo("so-theo-1001");
  }

  @Test
  void renderSummaryIncludesPhaseLabelsAndKeyLifecycleIds() {
    LifecycleTimeline timeline = new CommercialLifecycleTimelineBuilder().buildTheoLifecycleTimeline();

    String summary = timeline.renderSummary();

    assertThat(summary).contains("campaign");
    assertThat(summary).contains("campaign-ai-pets-launch");
    assertThat(summary).contains("so-theo-1001");
    assertThat(summary).contains("case-theo-color-mismatch");
    assertThat(summary).contains("ra-theo-1001");
    assertThat(summary).contains("refund-theo-1001");
  }
}
