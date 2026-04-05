package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record Opportunity(
    String externalId,
    String partyKey,
    String campaignExternalId,
    String title) {

  public Opportunity {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(title, "title");
  }
}
