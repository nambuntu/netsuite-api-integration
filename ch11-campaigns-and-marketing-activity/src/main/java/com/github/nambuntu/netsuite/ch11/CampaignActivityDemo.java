package com.github.nambuntu.netsuite.ch11;

import com.github.nambuntu.netsuite.ch11.command.CaptureCampaignResponseCommand;
import com.github.nambuntu.netsuite.ch11.command.ScheduleCampaignFollowUpCommand;
import com.github.nambuntu.netsuite.ch11.mock.InMemoryCampaignStore;
import com.github.nambuntu.netsuite.ch11.mock.InMemoryMarketingActivityStore;
import com.github.nambuntu.netsuite.ch11.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch11.model.Campaign;
import com.github.nambuntu.netsuite.ch11.model.CampaignActivityHistoryRow;
import com.github.nambuntu.netsuite.ch11.model.CampaignChannel;
import com.github.nambuntu.netsuite.ch11.model.CampaignStatus;
import com.github.nambuntu.netsuite.ch11.service.CampaignService;
import com.github.nambuntu.netsuite.ch11.service.MarketingActivityService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class CampaignActivityDemo {

  private static final String MAIN_CAMPAIGN_EXTERNAL_ID = "campaign-ai-pets-echo-owl-launch";

  private CampaignActivityDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    InMemoryCampaignStore campaignStore = new InMemoryCampaignStore();
    InMemoryMarketingActivityStore activityStore = new InMemoryMarketingActivityStore();
    CampaignService campaignService = new CampaignService(campaignStore);
    MarketingActivityService marketingActivityService =
        new MarketingActivityService(activityStore, new MockSuiteQlClient(activityStore));

    campaignService.upsertCampaign(new Campaign(
        MAIN_CAMPAIGN_EXTERNAL_ID,
        "Echo Owl launch",
        CampaignChannel.EMAIL,
        CampaignStatus.ACTIVE,
        new BigDecimal("2500.00")));
    campaignService.upsertCampaign(new Campaign(
        "campaign-ai-pets-nimbus-cat-launch",
        "Nimbus Cat launch",
        CampaignChannel.EMAIL,
        CampaignStatus.ACTIVE,
        new BigDecimal("1800.00")));

    marketingActivityService.recordCampaignResponse(new CaptureCampaignResponseCommand(
        "activity-theo-echo-owl-click-1",
        MAIN_CAMPAIGN_EXTERNAL_ID,
        "lead-theo-tran",
        Instant.parse("2026-04-01T09:00:00Z"),
        "requested updates"));
    marketingActivityService.scheduleFollowUp(new ScheduleCampaignFollowUpCommand(
        "activity-theo-echo-owl-demo-call-1",
        MAIN_CAMPAIGN_EXTERNAL_ID,
        "lead-theo-tran",
        "sales-ops",
        Instant.parse("2026-04-01T10:00:00Z"),
        "demo call scheduled"));

    List<CampaignActivityHistoryRow> history = marketingActivityService.findCampaignHistory(MAIN_CAMPAIGN_EXTERNAL_ID);
    return renderOutput(campaignService.findCampaignByExternalId(MAIN_CAMPAIGN_EXTERNAL_ID).orElseThrow(), history);
  }

  private static String renderOutput(Campaign campaign, List<CampaignActivityHistoryRow> history) {
    StringBuilder builder = new StringBuilder();
    builder.append("Campaign: ")
        .append(campaign.externalId())
        .append(" (")
        .append(campaign.channel())
        .append(", ")
        .append(campaign.status())
        .append(")")
        .append(System.lineSeparator())
        .append("History:")
        .append(System.lineSeparator());
    for (CampaignActivityHistoryRow row : history) {
      builder.append("- ")
          .append(row.occurredAt())
          .append(" ")
          .append(row.activityKind())
          .append(" ")
          .append(row.subjectExternalId())
          .append(" ")
          .append(row.note())
          .append(System.lineSeparator());
    }
    builder.setLength(builder.length() - System.lineSeparator().length());
    return builder.toString();
  }
}
