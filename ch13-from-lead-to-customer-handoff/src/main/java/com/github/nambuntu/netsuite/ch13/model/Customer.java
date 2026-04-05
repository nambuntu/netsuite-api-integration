package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record Customer(
    String externalId,
    String partyKey,
    String primaryEmail,
    String campaignExternalId,
    String opportunityExternalId) {

  public Customer {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(primaryEmail, "primaryEmail");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
  }
}
