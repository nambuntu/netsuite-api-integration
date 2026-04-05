package com.github.nambuntu.netsuite.ch18.mock;

import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import java.util.LinkedHashMap;
import java.util.Map;

public final class InMemoryFulfillmentGateway {

  private final Map<String, Integer> remainingFailuresByCommandId = new LinkedHashMap<>();
  private final Map<String, Integer> executionCountsByCommandId = new LinkedHashMap<>();

  public void configureFailTimes(String commandId, int failureCount) {
    remainingFailuresByCommandId.put(commandId, failureCount);
  }

  public String execute(FulfillmentCommand command) {
    executionCountsByCommandId.merge(command.commandId(), 1, Integer::sum);
    int remainingFailures = remainingFailuresByCommandId.getOrDefault(command.commandId(), 0);
    if (remainingFailures > 0) {
      remainingFailuresByCommandId.put(command.commandId(), remainingFailures - 1);
      throw new TransientGatewayException("transient gateway failure");
    }
    return command.fulfillmentExternalId();
  }

  public int executionCount(String commandId) {
    return executionCountsByCommandId.getOrDefault(commandId, 0);
  }
}
