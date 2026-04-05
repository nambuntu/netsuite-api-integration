package com.github.nambuntu.netsuite.ch11.mock;

import com.github.nambuntu.netsuite.ch11.model.Campaign;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryCampaignStore {

  private final Map<String, Campaign> campaignsByExternalId = new LinkedHashMap<>();

  public Campaign upsert(Campaign campaign) {
    campaignsByExternalId.put(campaign.externalId(), campaign);
    return campaign;
  }

  public Optional<Campaign> findByExternalId(String externalId) {
    return Optional.ofNullable(campaignsByExternalId.get(externalId));
  }
}
