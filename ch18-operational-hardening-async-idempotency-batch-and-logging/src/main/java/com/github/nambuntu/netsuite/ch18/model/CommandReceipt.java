package com.github.nambuntu.netsuite.ch18.model;

import java.util.Objects;

public record CommandReceipt(
    String commandId,
    String correlationId,
    String orderExternalId,
    CommandStatus status) {

  public CommandReceipt {
    Objects.requireNonNull(commandId, "commandId");
    Objects.requireNonNull(correlationId, "correlationId");
    Objects.requireNonNull(orderExternalId, "orderExternalId");
    Objects.requireNonNull(status, "status");
  }
}
