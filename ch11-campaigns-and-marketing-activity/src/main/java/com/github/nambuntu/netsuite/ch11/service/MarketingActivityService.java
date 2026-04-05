package com.github.nambuntu.netsuite.ch11.service;

import com.github.nambuntu.netsuite.ch11.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch11.command.CaptureCampaignResponseCommand;
import com.github.nambuntu.netsuite.ch11.command.ScheduleCampaignFollowUpCommand;
import com.github.nambuntu.netsuite.ch11.mock.InMemoryMarketingActivityStore;
import com.github.nambuntu.netsuite.ch11.model.CampaignActivityHistoryRow;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivity;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivityKind;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivityStatus;
import com.github.nambuntu.netsuite.ch11.query.CampaignActivityQueries;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MarketingActivityService {

  private final InMemoryMarketingActivityStore marketingActivityStore;
  private final SuiteQlClient suiteQlClient;

  public MarketingActivityService(
      InMemoryMarketingActivityStore marketingActivityStore,
      SuiteQlClient suiteQlClient) {
    this.marketingActivityStore = Objects.requireNonNull(marketingActivityStore, "marketingActivityStore");
    this.suiteQlClient = Objects.requireNonNull(suiteQlClient, "suiteQlClient");
  }

  public MarketingActivity recordCampaignResponse(CaptureCampaignResponseCommand command) {
    MarketingActivity activity = new MarketingActivity(
        command.externalId(),
        command.campaignExternalId(),
        command.leadExternalId(),
        MarketingActivityKind.RESPONSE_CAPTURED,
        MarketingActivityStatus.RECORDED,
        null,
        command.occurredAt(),
        command.note());
    return marketingActivityStore.upsert(activity);
  }

  public MarketingActivity scheduleFollowUp(ScheduleCampaignFollowUpCommand command) {
    MarketingActivity activity = new MarketingActivity(
        command.externalId(),
        command.campaignExternalId(),
        command.leadExternalId(),
        MarketingActivityKind.FOLLOW_UP_SCHEDULED,
        MarketingActivityStatus.SCHEDULED,
        command.owner(),
        command.occurredAt(),
        command.note());
    return marketingActivityStore.upsert(activity);
  }

  public List<CampaignActivityHistoryRow> findCampaignHistory(String campaignExternalId) {
    return suiteQlClient.query(
        CampaignActivityQueries.CAMPAIGN_ACTIVITY_HISTORY,
        Map.of("campaignExternalId", campaignExternalId));
  }
}
