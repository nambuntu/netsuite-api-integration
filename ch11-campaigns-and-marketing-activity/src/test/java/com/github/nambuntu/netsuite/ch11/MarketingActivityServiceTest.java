package com.github.nambuntu.netsuite.ch11;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch11.command.CaptureCampaignResponseCommand;
import com.github.nambuntu.netsuite.ch11.command.ScheduleCampaignFollowUpCommand;
import com.github.nambuntu.netsuite.ch11.mock.InMemoryMarketingActivityStore;
import com.github.nambuntu.netsuite.ch11.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch11.service.MarketingActivityService;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class MarketingActivityServiceTest {

  @Test
  void responseAndFollowUpAreLinkedToSameCampaignAndLead() {
    InMemoryMarketingActivityStore store = new InMemoryMarketingActivityStore();
    MarketingActivityService service = new MarketingActivityService(store, new MockSuiteQlClient(store));

    service.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-theo-echo-owl-click-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        Instant.parse("2026-04-01T09:00:00Z"),
        "requested updates"));
    service.scheduleFollowUp(new ScheduleCampaignFollowUpCommand(
        "activity-theo-echo-owl-demo-call-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        "sales-ops",
        Instant.parse("2026-04-01T10:00:00Z"),
        "demo call scheduled"));

    assertThat(service.findCampaignHistory("campaign-ai-pets-echo-owl-launch"))
        .extracting(row -> row.subjectExternalId() + " " + row.note())
        .containsExactly(
            "lead-theo-tran requested updates",
            "sales-ops demo call scheduled");
  }

  @Test
  void duplicateExternalIdUpdatesStoredActivityInsteadOfCreatingDuplicate() {
    InMemoryMarketingActivityStore store = new InMemoryMarketingActivityStore();
    MarketingActivityService service = new MarketingActivityService(store, new MockSuiteQlClient(store));

    service.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-theo-echo-owl-click-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        Instant.parse("2026-04-01T09:00:00Z"),
        "requested updates"));
    service.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-theo-echo-owl-click-1",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        Instant.parse("2026-04-01T09:00:00Z"),
        "requested product demo"));

    assertThat(service.findCampaignHistory("campaign-ai-pets-echo-owl-launch"))
        .singleElement()
        .satisfies(row -> {
          assertThat(row.subjectExternalId()).isEqualTo("lead-theo-tran");
          assertThat(row.note()).isEqualTo("requested product demo");
        });
  }
}
