package com.github.nambuntu.netsuite.ch12.mock;

import com.github.nambuntu.netsuite.ch12.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch12.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.query.OpportunityQueries;
import com.github.nambuntu.netsuite.ch12.query.SuiteQlQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MockSuiteQlClient implements SuiteQlClient {

  private final InMemoryOpportunityStore opportunityStore;

  public MockSuiteQlClient(InMemoryOpportunityStore opportunityStore) {
    this.opportunityStore = Objects.requireNonNull(opportunityStore, "opportunityStore");
  }

  @Override
  public <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params) {
    if (OpportunityQueries.OPEN_OPPORTUNITIES_BY_CRM_PARTY.id().equals(query.id())) {
      String crmPartyExternalId = Objects.toString(params.get("crmPartyExternalId"));
      return opportunityStore.findOpenByCrmPartyExternalId(crmPartyExternalId).stream()
          .map(this::toRow)
          .map(query::mapRow)
          .toList();
    }
    throw new IllegalArgumentException("Unsupported query id: " + query.id());
  }

  private SuiteQlRow toRow(OpenOpportunitySummary summary) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("opportunity_external_id", summary.opportunityExternalId());
    row.put("crm_party_external_id", summary.crmPartyExternalId());
    row.put("status", summary.status().name());
    row.put("projected_amount", summary.projectedAmount());
    row.put("probability", summary.probability());
    row.put("expected_close_date", summary.expectedCloseDate());
    return new SuiteQlRow(row);
  }
}
