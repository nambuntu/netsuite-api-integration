package com.github.nambuntu.netsuite.ch14;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SupportCaseTimelineDemoTest {

  @Test
  void renderDemoPrintsReadableCaseLifecycle() {
    assertThat(SupportCaseTimelineDemo.renderDemo()).isEqualTo("""
        Opened support case: case-theo-color-mismatch for customer-theo-tran order=so-theo-1001
        Open cases:
         - case-theo-color-mismatch [OPEN] priority=HIGH assignedTo=unassigned

        Updated support case: case-theo-color-mismatch assignedTo=jules-ops status=IN_PROGRESS
        Closed support case: case-theo-color-mismatch status=CLOSED

        Case timeline:
         - OPENED by mira-agent: Wrong color and too-chatty personality pack reported
         - ASSIGNED by mira-agent -> jules-ops: Routed to operations for shipment mismatch review
         - UPDATED by jules-ops: Confirmed the wrong finish and personality pack mismatch
         - CLOSED by mira-agent: Replacement arranged and customer notified""");
  }
}
