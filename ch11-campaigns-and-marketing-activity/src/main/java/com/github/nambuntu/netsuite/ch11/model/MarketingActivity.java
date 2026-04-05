package com.github.nambuntu.netsuite.ch11.model;

import java.time.Instant;
import java.util.Objects;

public record MarketingActivity(
    String externalId,
    String campaignExternalId,
    String partyExternalId,
    MarketingActivityKind kind,
    MarketingActivityStatus status,
    String owner,
    Instant occurredAt,
    String note) {

  public MarketingActivity {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(partyExternalId, "partyExternalId");
    Objects.requireNonNull(kind, "kind");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(occurredAt, "occurredAt");
    Objects.requireNonNull(note, "note");
  }
}
