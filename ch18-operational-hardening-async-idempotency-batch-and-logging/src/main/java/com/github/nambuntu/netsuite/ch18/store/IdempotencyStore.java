package com.github.nambuntu.netsuite.ch18.store;

import com.github.nambuntu.netsuite.ch18.logging.WorkflowLogContext;
import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandReceipt;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class IdempotencyStore {

  private final Map<String, CommandReceipt> receiptsByCommandId = new LinkedHashMap<>();
  private final Map<String, CommandOutcome> outcomesByCommandId = new LinkedHashMap<>();

  public synchronized CommandReceipt recordReceipt(FulfillmentCommand command, WorkflowLogContext context) {
    Objects.requireNonNull(command, "command");
    Objects.requireNonNull(context, "context");
    CommandReceipt existing = receiptsByCommandId.get(command.commandId());
    if (existing != null) {
      return existing;
    }
    CommandReceipt receipt = new CommandReceipt(
        command.commandId(),
        context.correlationId(),
        command.orderExternalId(),
        CommandStatus.RECEIVED);
    receiptsByCommandId.put(command.commandId(), receipt);
    return receipt;
  }

  public synchronized Optional<CommandReceipt> findReceipt(String commandId) {
    return Optional.ofNullable(receiptsByCommandId.get(commandId));
  }

  public synchronized void recordOutcome(CommandOutcome outcome) {
    Objects.requireNonNull(outcome, "outcome");
    outcomesByCommandId.put(outcome.commandId(), outcome);
  }

  public synchronized Optional<CommandOutcome> findOutcome(String commandId) {
    return Optional.ofNullable(outcomesByCommandId.get(commandId));
  }
}
