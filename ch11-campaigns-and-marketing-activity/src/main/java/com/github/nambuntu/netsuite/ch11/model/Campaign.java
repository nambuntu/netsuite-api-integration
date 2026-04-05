package com.github.nambuntu.netsuite.ch11.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Campaign(
    String externalId,
    String name,
    CampaignChannel channel,
    CampaignStatus status,
    BigDecimal budget) {

  public Campaign {
    Objects.requireNonNull(externalId, "externalId");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(channel, "channel");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(budget, "budget");
  }
}
