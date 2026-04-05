package com.github.nambuntu.netsuite.ch11.model;

import java.time.Instant;
import java.util.Objects;

public record CampaignActivityHistoryRow(
    Instant occurredAt,
    MarketingActivityKind activityKind,
    String subjectExternalId,
    String note) {

  public CampaignActivityHistoryRow {
    Objects.requireNonNull(occurredAt, "occurredAt");
    Objects.requireNonNull(activityKind, "activityKind");
    Objects.requireNonNull(subjectExternalId, "subjectExternalId");
    Objects.requireNonNull(note, "note");
  }
}
