package com.github.nambuntu.netsuite.ch12.model;

import java.math.BigDecimal;
import java.util.Objects;

public record QualifiedCrmState(
    String crmPartyExternalId,
    CrmPartyStage stageBefore,
    CrmPartyStage stageAfter,
    CrmPartyStatus statusBefore,
    CrmPartyStatus statusAfter,
    String opportunityExternalId,
    OpportunityStatus opportunityStatus,
    BigDecimal projectedAmount,
    BigDecimal probability,
    int openOpportunityCount) {

  public QualifiedCrmState {
    Objects.requireNonNull(crmPartyExternalId, "crmPartyExternalId");
    Objects.requireNonNull(stageBefore, "stageBefore");
    Objects.requireNonNull(stageAfter, "stageAfter");
    Objects.requireNonNull(statusBefore, "statusBefore");
    Objects.requireNonNull(statusAfter, "statusAfter");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(opportunityStatus, "opportunityStatus");
    Objects.requireNonNull(projectedAmount, "projectedAmount");
    Objects.requireNonNull(probability, "probability");
  }
}
