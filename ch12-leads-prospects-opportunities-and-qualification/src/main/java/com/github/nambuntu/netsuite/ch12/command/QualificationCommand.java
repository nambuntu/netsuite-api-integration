package com.github.nambuntu.netsuite.ch12.command;

import com.github.nambuntu.netsuite.ch12.model.CrmPartyStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record QualificationCommand(
    String crmPartyExternalId,
    String name,
    String email,
    String campaignExternalId,
    CrmPartyStatus leadStatus,
    CrmPartyStatus prospectStatus,
    String opportunityExternalId,
    String opportunityTitle,
    BigDecimal projectedAmount,
    BigDecimal probability,
    LocalDate expectedCloseDate) {

  public QualificationCommand {
    Objects.requireNonNull(crmPartyExternalId, "crmPartyExternalId");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadStatus, "leadStatus");
    Objects.requireNonNull(prospectStatus, "prospectStatus");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(opportunityTitle, "opportunityTitle");
    Objects.requireNonNull(projectedAmount, "projectedAmount");
    Objects.requireNonNull(probability, "probability");
    Objects.requireNonNull(expectedCloseDate, "expectedCloseDate");
  }
}
