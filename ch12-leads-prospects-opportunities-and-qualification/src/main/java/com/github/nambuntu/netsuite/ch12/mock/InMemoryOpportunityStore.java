package com.github.nambuntu.netsuite.ch12.mock;

import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.model.OpportunityDraft;
import com.github.nambuntu.netsuite.ch12.model.OpportunityStatus;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryOpportunityStore {

  private final Map<String, OpenOpportunitySummary> opportunitiesByExternalId = new LinkedHashMap<>();

  public OpenOpportunitySummary upsertOpenOpportunity(OpportunityDraft draft) {
    OpenOpportunitySummary summary = new OpenOpportunitySummary(
        draft.externalId(),
        draft.crmPartyExternalId(),
        OpportunityStatus.OPEN,
        draft.projectedAmount(),
        draft.probability(),
        draft.expectedCloseDate());
    opportunitiesByExternalId.put(summary.opportunityExternalId(), summary);
    return summary;
  }

  public OpenOpportunitySummary save(OpenOpportunitySummary summary) {
    opportunitiesByExternalId.put(summary.opportunityExternalId(), summary);
    return summary;
  }

  public List<OpenOpportunitySummary> findOpenByCrmPartyExternalId(String crmPartyExternalId) {
    return opportunitiesByExternalId.values().stream()
        .filter(summary -> summary.crmPartyExternalId().equals(crmPartyExternalId))
        .filter(summary -> summary.status() == OpportunityStatus.OPEN)
        .sorted(Comparator.comparing(OpenOpportunitySummary::opportunityExternalId))
        .toList();
  }

  public Optional<OpenOpportunitySummary> findByExternalId(String externalId) {
    return Optional.ofNullable(opportunitiesByExternalId.get(externalId));
  }

  public int count() {
    return opportunitiesByExternalId.size();
  }
}
