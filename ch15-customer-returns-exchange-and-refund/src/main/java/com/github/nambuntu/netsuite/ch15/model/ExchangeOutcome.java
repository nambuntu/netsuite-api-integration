package com.github.nambuntu.netsuite.ch15.model;

import java.time.Instant;

public record ExchangeOutcome(
    String replacementSalesOrderExternalId,
    String replacementFulfillmentExternalId,
    String description,
    Instant completedAt) {
}
