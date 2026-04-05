package com.github.nambuntu.netsuite.ch15.command;

import java.time.Instant;

public record CompleteExchangeCommand(
    String returnAuthorizationExternalId,
    String replacementSalesOrderExternalId,
    String replacementFulfillmentExternalId,
    String replacementDescription,
    Instant completedAt) {
}
