package com.github.nambuntu.netsuite.ch16;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TransformationWorkflowDemoTest {

  @Test
  void renderDemoPrintsTransformContinuityAndManualWarning() {
    assertThat(TransformationWorkflowDemo.renderDemo()).isEqualTo("""
        source order: so-theo-1001
        derived invoice: inv-theo-1001
        same customer: customer-theo-tran
        invoice source link: so-theo-1001
        derived from source lines: yes
        patched fields: memo, location, supportCaseLink
        derived fulfillment: if-theo-1001
        fulfillment source link: so-theo-1001
        return from sales order: ra-theo-1001
        return sales-order link: so-theo-1001
        return from fulfillment: ra-theo-1001-from-fulfillment
        return fulfillment link: if-theo-1001
        manual assembly warning: source linkage must be re-copied explicitly""");
  }
}
