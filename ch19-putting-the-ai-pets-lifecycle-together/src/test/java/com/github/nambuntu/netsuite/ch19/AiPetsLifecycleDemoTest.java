package com.github.nambuntu.netsuite.ch19;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AiPetsLifecycleDemoTest {

  @Test
  void renderDemoPrintsTheExpectedLinkedLifecycleStory() {
    assertThat(AiPetsLifecycleDemo.renderDemo()).isEqualTo("""
        campaign-ai-pets-launch -> lead-theo-tran -> customer-theo-tran
        customer-theo-tran -> so-theo-1001 -> inv-theo-1001 -> payment-theo-1001
        so-theo-1001 -> fulfill-theo-1001
        so-theo-1001 -> case-theo-color-mismatch -> ra-theo-1001 -> replacement-theo-1001
        ra-theo-1001 -> refund-theo-1001 -> case-closed""");
  }
}
