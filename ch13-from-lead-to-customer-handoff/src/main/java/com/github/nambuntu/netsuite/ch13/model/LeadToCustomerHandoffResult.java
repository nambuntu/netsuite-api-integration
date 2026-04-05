package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record LeadToCustomerHandoffResult(
    String handoffStatus,
    String partyKey,
    String leadExternalId,
    String prospectExternalId,
    String opportunityExternalId,
    String contactExternalId,
    String customerExternalId,
    String continuityStatus,
    String notes) {

  public LeadToCustomerHandoffResult {
    Objects.requireNonNull(handoffStatus, "handoffStatus");
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(prospectExternalId, "prospectExternalId");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(contactExternalId, "contactExternalId");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
    Objects.requireNonNull(continuityStatus, "continuityStatus");
    Objects.requireNonNull(notes, "notes");
  }

  public static LeadToCustomerHandoffResult from(
      HandoffIdentity identity,
      HandoffContinuityView continuityView) {
    return new LeadToCustomerHandoffResult(
        "SUCCESS",
        identity.partyKey(),
        identity.leadExternalId(),
        identity.prospectExternalId(),
        identity.opportunityExternalId(),
        identity.contactExternalId(),
        identity.customerExternalId(),
        continuityView.continuityStatus(),
        continuityView.continuityNote());
  }
}
