package com.github.nambuntu.netsuite.ch12.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record OpenOpportunitySummary(
    String opportunityExternalId,
    String crmPartyExternalId,
    OpportunityStatus status,
    BigDecimal projectedAmount,
    BigDecimal probability,
    LocalDate expectedCloseDate) {

  public OpenOpportunitySummary {
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(crmPartyExternalId, "crmPartyExternalId");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(projectedAmount, "projectedAmount");
    Objects.requireNonNull(probability, "probability");
    Objects.requireNonNull(expectedCloseDate, "expectedCloseDate");
  }
}
