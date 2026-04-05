package com.github.nambuntu.netsuite.ch11.command;

import java.time.Instant;
import java.util.Objects;

public record ScheduleCampaignFollowUpCommand(
    String externalId,
    String campaignExternalId,
    String leadExternalId,
    String owner,
    Instant occurredAt,
    String note) {

  public ScheduleCampaignFollowUpCommand {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(owner, "owner");
    Objects.requireNonNull(occurredAt, "occurredAt");
    Objects.requireNonNull(note, "note");
  }
}
