package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record Lead(
    String externalId,
    String partyKey,
    String campaignExternalId,
    String email) {

  public Lead {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(email, "email");
  }
}
