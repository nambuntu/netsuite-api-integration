package com.github.nambuntu.netsuite.ch11;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CampaignActivityDemoTest {

  @Test
  void renderDemoPrintsCompactCampaignHistory() {
    assertThat(CampaignActivityDemo.renderDemo()).isEqualTo("""
        Campaign: campaign-ai-pets-echo-owl-launch (EMAIL, ACTIVE)
        History:
        - 2026-04-01T09:00:00Z RESPONSE_CAPTURED lead-theo-tran requested updates
        - 2026-04-01T10:00:00Z FOLLOW_UP_SCHEDULED sales-ops demo call scheduled""");
  }
}
