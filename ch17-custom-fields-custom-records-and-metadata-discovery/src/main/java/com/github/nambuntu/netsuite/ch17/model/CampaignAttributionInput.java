package com.github.nambuntu.netsuite.ch17.model;

import java.math.BigDecimal;
import java.util.Objects;

public record CampaignAttributionInput(
    String externalId,
    String title,
    String status,
    String owner,
    BigDecimal budget,
    String acquisitionSourceKey,
    String regionBucket,
    String reportingQuarter) {

  public CampaignAttributionInput {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(title, "title");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(owner, "owner");
    Objects.requireNonNull(budget, "budget");
  }
}
