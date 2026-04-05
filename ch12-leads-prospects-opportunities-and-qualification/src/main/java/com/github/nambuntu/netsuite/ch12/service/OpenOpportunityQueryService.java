package com.github.nambuntu.netsuite.ch12.service;

import com.github.nambuntu.netsuite.ch12.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.query.OpportunityQueries;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class OpenOpportunityQueryService {

  private final SuiteQlClient suiteQlClient;

  public OpenOpportunityQueryService(SuiteQlClient suiteQlClient) {
    this.suiteQlClient = Objects.requireNonNull(suiteQlClient, "suiteQlClient");
  }

  public List<OpenOpportunitySummary> findOpenOpportunities(String crmPartyExternalId) {
    return suiteQlClient.query(
        OpportunityQueries.OPEN_OPPORTUNITIES_BY_CRM_PARTY,
        Map.of("crmPartyExternalId", crmPartyExternalId));
  }
}
