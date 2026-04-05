package com.github.nambuntu.netsuite.ch12.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record OpportunityDraft(
    String externalId,
    String crmPartyExternalId,
    String title,
    BigDecimal projectedAmount,
    BigDecimal probability,
    LocalDate expectedCloseDate) {

  public OpportunityDraft {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(crmPartyExternalId, "crmPartyExternalId");
    Objects.requireNonNull(title, "title");
    Objects.requireNonNull(projectedAmount, "projectedAmount");
    Objects.requireNonNull(probability, "probability");
    Objects.requireNonNull(expectedCloseDate, "expectedCloseDate");
  }
}
