package com.github.nambuntu.netsuite.ch12.query;

import com.github.nambuntu.netsuite.ch12.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.model.OpportunityStatus;

public final class OpportunityQueries {

  public static final SuiteQlQuery<OpenOpportunitySummary> OPEN_OPPORTUNITIES_BY_CRM_PARTY = new SuiteQlQuery<>(
      "crm.openOpportunitiesByParty",
      """
      SELECT
        opportunity.externalid AS opportunity_external_id,
        opportunity.crmpartyexternalid AS crm_party_external_id,
        opportunity.status AS status,
        opportunity.projectedamount AS projected_amount,
        opportunity.probability AS probability,
        opportunity.expectedclosedate AS expected_close_date
      FROM open_opportunity_view
      WHERE opportunity.crmpartyexternalid = :crmPartyExternalId
      AND opportunity.status = 'OPEN'
      ORDER BY opportunity.externalid
      """,
      OpportunityQueries::mapOpenOpportunitySummary);

  private OpportunityQueries() {
  }

  public static OpenOpportunitySummary mapOpenOpportunitySummary(SuiteQlRow row) {
    return new OpenOpportunitySummary(
        row.string("opportunity_external_id"),
        row.string("crm_party_external_id"),
        OpportunityStatus.valueOf(row.string("status")),
        row.decimal("projected_amount"),
        row.decimal("probability"),
        row.localDate("expected_close_date"));
  }
}
