package com.github.nambuntu.netsuite.ch12.service;

import com.github.nambuntu.netsuite.ch12.mock.InMemoryCrmPartyStore;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryOpportunityStore;
import com.github.nambuntu.netsuite.ch12.model.CrmParty;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStage;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStatus;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.model.OpportunityDraft;
import java.util.Objects;
import java.util.Optional;

public final class CrmQualificationService {

  private final InMemoryCrmPartyStore crmPartyStore;
  private final InMemoryOpportunityStore opportunityStore;

  public CrmQualificationService(
      InMemoryCrmPartyStore crmPartyStore,
      InMemoryOpportunityStore opportunityStore) {
    this.crmPartyStore = Objects.requireNonNull(crmPartyStore, "crmPartyStore");
    this.opportunityStore = Objects.requireNonNull(opportunityStore, "opportunityStore");
  }

  public CrmParty upsertLead(CrmParty crmParty) {
    Optional<CrmParty> existing = crmPartyStore.findByExternalId(crmParty.externalId());
    if (existing.isEmpty()) {
      return crmPartyStore.upsert(crmParty);
    }
    CrmParty current = existing.orElseThrow();
    if (stageRank(current.stage()) > stageRank(crmParty.stage())) {
      return crmPartyStore.upsert(new CrmParty(
          current.externalId(),
          crmParty.name(),
          crmParty.email(),
          crmParty.campaignExternalId(),
          current.stage(),
          current.status()));
    }
    return crmPartyStore.upsert(crmParty);
  }

  public CrmParty promoteToProspect(String crmPartyExternalId, CrmPartyStatus status) {
    CrmParty current = crmPartyStore.findByExternalId(crmPartyExternalId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown CRM party: " + crmPartyExternalId));
    CrmParty promoted = new CrmParty(
        current.externalId(),
        current.name(),
        current.email(),
        current.campaignExternalId(),
        CrmPartyStage.PROSPECT,
        status);
    return crmPartyStore.upsert(promoted);
  }

  public OpenOpportunitySummary openOpportunity(OpportunityDraft draft) {
    return opportunityStore.upsertOpenOpportunity(draft);
  }

  public Optional<CrmParty> findCrmPartyByExternalId(String externalId) {
    return crmPartyStore.findByExternalId(externalId);
  }

  private static int stageRank(CrmPartyStage stage) {
    return switch (stage) {
      case LEAD -> 0;
      case PROSPECT -> 1;
      case CUSTOMER -> 2;
    };
  }
}
