package com.github.nambuntu.netsuite.ch11.mock;

import com.github.nambuntu.netsuite.ch11.model.MarketingActivity;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryMarketingActivityStore {

  private final Map<String, MarketingActivity> activitiesByExternalId = new LinkedHashMap<>();

  public MarketingActivity upsert(MarketingActivity activity) {
    activitiesByExternalId.put(activity.externalId(), activity);
    return activity;
  }

  public List<MarketingActivity> findByCampaignExternalId(String campaignExternalId) {
    return activitiesByExternalId.values().stream()
        .filter(activity -> activity.campaignExternalId().equals(campaignExternalId))
        .sorted(Comparator.comparing(MarketingActivity::occurredAt)
            .thenComparing(MarketingActivity::externalId))
        .toList();
  }
}
