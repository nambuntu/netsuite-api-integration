package com.github.nambuntu.netsuite.ch18.model;

import java.util.Objects;

public record CommandOutcome(
    String commandId,
    String correlationId,
    String orderExternalId,
    String fulfillmentExternalId,
    CommandStatus status,
    int attempts,
    boolean deduplicated,
    String message) {

  public CommandOutcome {
    Objects.requireNonNull(commandId, "commandId");
    Objects.requireNonNull(correlationId, "correlationId");
    Objects.requireNonNull(orderExternalId, "orderExternalId");
    Objects.requireNonNull(fulfillmentExternalId, "fulfillmentExternalId");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(message, "message");
  }
}
