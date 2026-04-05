package com.github.nambuntu.netsuite.ch11.command;

import java.time.Instant;
import java.util.Objects;

public record CaptureCampaignResponseCommand(
    String externalId,
    String campaignExternalId,
    String leadExternalId,
    Instant occurredAt,
    String note) {

  public CaptureCampaignResponseCommand {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(occurredAt, "occurredAt");
    Objects.requireNonNull(note, "note");
  }
}
