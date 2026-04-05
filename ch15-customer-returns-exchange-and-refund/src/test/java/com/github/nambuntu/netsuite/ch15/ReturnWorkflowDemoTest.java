package com.github.nambuntu.netsuite.ch15;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReturnWorkflowDemoTest {

  @Test
  void renderDemoPrintsCompactReverseWorkflowTimeline() {
    assertThat(ReturnWorkflowDemo.renderDemo()).isEqualTo("""
        case-theo-color-mismatch
          01 OPENED            Theo reports wrong color and overly chatty personality pack
          02 RETURN_AUTH       ra-theo-1001-exchange linkedTo=so-theo-1001 disposition=EXCHANGE
          03 RECEIVED          ra-theo-1001-exchange
          04 REPLACEMENT_ORDER so-theo-1001-r1 created for quiet-pack Nimbus Cat
          05 REPLACEMENT_SHIP  if-theo-1001-r1 shipped
          06 RETURN_AUTH       ra-theo-1001-final linkedTo=so-theo-1001-r1 disposition=REFUND
          07 RECEIVED          ra-theo-1001-final
          08 CREDIT_ISSUED     cm-theo-1001-final amount=249.00
          09 CUSTOMER_REFUND   refund-theo-1001-final amount=249.00
          10 CASE_CLOSED       Theo refunded and case resolved""");
  }
}
