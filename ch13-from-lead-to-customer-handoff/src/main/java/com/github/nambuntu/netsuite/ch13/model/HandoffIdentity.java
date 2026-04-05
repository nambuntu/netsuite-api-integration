package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record HandoffIdentity(
    String partyKey,
    String leadExternalId,
    String prospectExternalId,
    String opportunityExternalId,
    String contactExternalId,
    String customerExternalId) {

  public HandoffIdentity {
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(prospectExternalId, "prospectExternalId");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(contactExternalId, "contactExternalId");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
  }
}
