package com.github.nambuntu.netsuite.ch11.mock;

import com.github.nambuntu.netsuite.ch11.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch11.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivity;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivityKind;
import com.github.nambuntu.netsuite.ch11.query.CampaignActivityQueries;
import com.github.nambuntu.netsuite.ch11.query.SuiteQlQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MockSuiteQlClient implements SuiteQlClient {

  private final InMemoryMarketingActivityStore marketingActivityStore;

  public MockSuiteQlClient(InMemoryMarketingActivityStore marketingActivityStore) {
    this.marketingActivityStore = Objects.requireNonNull(marketingActivityStore, "marketingActivityStore");
  }

  @Override
  public <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params) {
    if (CampaignActivityQueries.CAMPAIGN_ACTIVITY_HISTORY.id().equals(query.id())) {
      String campaignExternalId = Objects.toString(params.get("campaignExternalId"));
      return marketingActivityStore.findByCampaignExternalId(campaignExternalId).stream()
          .map(this::toHistoryRow)
          .map(query::mapRow)
          .toList();
    }
    throw new IllegalArgumentException("Unsupported query id: " + query.id());
  }

  private SuiteQlRow toHistoryRow(MarketingActivity activity) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("occurred_at", activity.occurredAt());
    row.put("activity_kind", activity.kind().name());
    row.put("subject_external_id", subjectExternalId(activity));
    row.put("note", activity.note());
    return new SuiteQlRow(row);
  }

  private String subjectExternalId(MarketingActivity activity) {
    return activity.kind() == MarketingActivityKind.FOLLOW_UP_SCHEDULED
        ? activity.owner()
        : activity.partyExternalId();
  }
}
