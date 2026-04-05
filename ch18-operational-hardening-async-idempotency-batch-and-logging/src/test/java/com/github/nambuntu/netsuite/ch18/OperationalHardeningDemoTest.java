package com.github.nambuntu.netsuite.ch18;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OperationalHardeningDemoTest {

  @Test
  void renderDemoShowsRetryDuplicateSuppressionBatchAndJournal() {
    assertThat(OperationalHardeningDemo.renderDemo()).isEqualTo("""
        correlation=wf-2026-04-01-001
        command=fulfill-order:so-theo-1001:shipment-1
        attempts=2
        deduplicated=yes
        final-status=completed
        replay-status=already-completed
        batch-chunk=1/1
        batch-item=fulfill-order:so-theo-1001:shipment-1 status=already-completed
        batch-item=fulfill-order:so-theo-1002:shipment-1 status=completed
        journal=submitted -> started#1 -> retrying#1 -> started#2 -> completed#2 -> duplicate-suppressed#2""");
  }
}
