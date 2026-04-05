package com.github.nambuntu.netsuite.ch18.logging;

import java.util.Objects;

public record WorkflowLogContext(String correlationId, String commandId, String orderExternalId) {

  public WorkflowLogContext {
    Objects.requireNonNull(correlationId, "correlationId");
    Objects.requireNonNull(commandId, "commandId");
    Objects.requireNonNull(orderExternalId, "orderExternalId");
  }
}
