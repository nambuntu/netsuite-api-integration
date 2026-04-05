package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record HandoffContinuityView(
    String partyKey,
    String campaignExternalId,
    String leadExternalId,
    String prospectExternalId,
    String opportunityExternalId,
    String contactExternalId,
    String customerExternalId,
    String contactEmail,
    String customerEmail,
    String continuityStatus,
    String continuityNote) {

  public HandoffContinuityView {
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(prospectExternalId, "prospectExternalId");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(contactExternalId, "contactExternalId");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
    Objects.requireNonNull(contactEmail, "contactEmail");
    Objects.requireNonNull(customerEmail, "customerEmail");
    Objects.requireNonNull(continuityStatus, "continuityStatus");
    Objects.requireNonNull(continuityNote, "continuityNote");
  }
}
