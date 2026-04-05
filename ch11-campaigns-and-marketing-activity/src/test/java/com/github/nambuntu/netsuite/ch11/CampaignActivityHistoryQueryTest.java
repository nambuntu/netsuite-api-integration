package com.github.nambuntu.netsuite.ch11;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch11.command.CaptureCampaignResponseCommand;
import com.github.nambuntu.netsuite.ch11.command.ScheduleCampaignFollowUpCommand;
import com.github.nambuntu.netsuite.ch11.mock.InMemoryMarketingActivityStore;
import com.github.nambuntu.netsuite.ch11.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivityKind;
import com.github.nambuntu.netsuite.ch11.service.MarketingActivityService;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CampaignActivityHistoryQueryTest {

  @Test
  void historyQueryReturnsExpectedRowsInTimestampOrderWithoutCampaignLeakage() {
    InMemoryMarketingActivityStore store = new InMemoryMarketingActivityStore();
    MarketingActivityService service = new MarketingActivityService(store, new MockSuiteQlClient(store));

    service.scheduleFollowUp(new ScheduleCampaignFollowUpCommand(
        "activity-theo-echo-owl-demo-call-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        "sales-ops",
        Instant.parse("2026-04-01T10:00:00Z"),
        "demo call scheduled"));
    service.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-theo-echo-owl-click-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        Instant.parse("2026-04-01T09:00:00Z"),
        "requested updates"));
    service.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-ava-nimbus-cat-click-1",
        "campaign-ai-pets-nimbus-cat-launch",
        "lead-ava-cole",
        Instant.parse("2026-04-01T08:30:00Z"),
        "opened launch email"));

    assertThat(service.findCampaignHistory("campaign-ai-pets-echo-owl-launch"))
        .hasSize(2)
        .extracting(row -> row.activityKind() + " " + row.subjectExternalId() + " " + row.note())
        .containsExactly(
            MarketingActivityKind.RESPONSE_CAPTURED + " lead-theo-tran requested updates",
            MarketingActivityKind.FOLLOW_UP_SCHEDULED + " sales-ops demo call scheduled");
  }
}
