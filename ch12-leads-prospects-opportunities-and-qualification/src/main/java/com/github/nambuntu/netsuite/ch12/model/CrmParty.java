package com.github.nambuntu.netsuite.ch12.model;

import java.util.Objects;

public record CrmParty(
    String externalId,
    String name,
    String email,
    String campaignExternalId,
    CrmPartyStage stage,
    CrmPartyStatus status) {

  public CrmParty {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(stage, "stage");
    Objects.requireNonNull(status, "status");
  }
}
