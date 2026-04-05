package com.github.nambuntu.netsuite.ch18.model;

import java.util.Objects;

public record FulfillmentCommand(String commandId, String orderExternalId, String fulfillmentExternalId) {

  public FulfillmentCommand {
    Objects.requireNonNull(commandId, "commandId");
    Objects.requireNonNull(orderExternalId, "orderExternalId");
    Objects.requireNonNull(fulfillmentExternalId, "fulfillmentExternalId");
  }
}
