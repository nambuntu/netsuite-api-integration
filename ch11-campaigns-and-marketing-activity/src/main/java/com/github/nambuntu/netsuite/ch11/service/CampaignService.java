package com.github.nambuntu.netsuite.ch11.service;

import com.github.nambuntu.netsuite.ch11.mock.InMemoryCampaignStore;
import com.github.nambuntu.netsuite.ch11.model.Campaign;
import java.util.Objects;
import java.util.Optional;

public final class CampaignService {

  private final InMemoryCampaignStore campaignStore;

  public CampaignService(InMemoryCampaignStore campaignStore) {
    this.campaignStore = Objects.requireNonNull(campaignStore, "campaignStore");
  }

  public Campaign upsertCampaign(Campaign campaign) {
    return campaignStore.upsert(campaign);
  }

  public Optional<Campaign> findCampaignByExternalId(String externalId) {
    return campaignStore.findByExternalId(externalId);
  }
}
