package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record Contact(
    String externalId,
    String partyKey,
    String primaryEmail,
    String customerExternalId) {

  public Contact {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(primaryEmail, "primaryEmail");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
  }
}
