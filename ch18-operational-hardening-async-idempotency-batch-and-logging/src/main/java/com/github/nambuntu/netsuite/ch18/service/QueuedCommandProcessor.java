package com.github.nambuntu.netsuite.ch18.service;

import com.github.nambuntu.netsuite.ch18.logging.WorkflowLogContext;
import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandReceipt;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import com.github.nambuntu.netsuite.ch18.mock.InMemoryFulfillmentGateway;
import com.github.nambuntu.netsuite.ch18.mock.TransientGatewayException;
import com.github.nambuntu.netsuite.ch18.policy.BoundedRetryPolicy;
import com.github.nambuntu.netsuite.ch18.store.IdempotencyStore;
import java.util.Objects;

public final class QueuedCommandProcessor {

  private final IdempotencyStore idempotencyStore;
  private final BoundedRetryPolicy retryPolicy;
  private final WorkflowJournal workflowJournal;
  private final InMemoryFulfillmentGateway fulfillmentGateway;
  private int correlationSequence;

  public QueuedCommandProcessor(
      IdempotencyStore idempotencyStore,
      BoundedRetryPolicy retryPolicy,
      WorkflowJournal workflowJournal,
      InMemoryFulfillmentGateway fulfillmentGateway) {
    this.idempotencyStore = Objects.requireNonNull(idempotencyStore, "idempotencyStore");
    this.retryPolicy = Objects.requireNonNull(retryPolicy, "retryPolicy");
    this.workflowJournal = Objects.requireNonNull(workflowJournal, "workflowJournal");
    this.fulfillmentGateway = Objects.requireNonNull(fulfillmentGateway, "fulfillmentGateway");
  }

  public CommandReceipt submit(FulfillmentCommand command) {
    Objects.requireNonNull(command, "command");
    return idempotencyStore.findReceipt(command.commandId())
        .orElseGet(() -> {
          WorkflowLogContext context = new WorkflowLogContext(
              nextCorrelationId(),
              command.commandId(),
              command.orderExternalId());
          CommandReceipt receipt = idempotencyStore.recordReceipt(command, context);
          workflowJournal.append(context, "submitted", 0, CommandStatus.RECEIVED, "command accepted");
          return receipt;
        });
  }

  public CommandOutcome process(FulfillmentCommand command) {
    Objects.requireNonNull(command, "command");
    CommandReceipt receipt = idempotencyStore.findReceipt(command.commandId()).orElseGet(() -> submit(command));
    WorkflowLogContext context = new WorkflowLogContext(
        receipt.correlationId(),
        receipt.commandId(),
        receipt.orderExternalId());

    CommandOutcome existingOutcome = idempotencyStore.findOutcome(command.commandId()).orElse(null);
    if (existingOutcome != null && existingOutcome.status() == CommandStatus.COMPLETED) {
      workflowJournal.append(
          context,
          "duplicate-suppressed",
          existingOutcome.attempts(),
          CommandStatus.ALREADY_COMPLETED,
          "command already completed");
      return new CommandOutcome(
          command.commandId(),
          receipt.correlationId(),
          command.orderExternalId(),
          existingOutcome.fulfillmentExternalId(),
          CommandStatus.ALREADY_COMPLETED,
          existingOutcome.attempts(),
          true,
          "already completed");
    }

    for (int attempt = 1; attempt <= retryPolicy.maxAttempts(); attempt++) {
      workflowJournal.append(context, "started", attempt, CommandStatus.IN_PROGRESS, "dispatching fulfillment");
      try {
        String fulfillmentExternalId = fulfillmentGateway.execute(command);
        CommandOutcome outcome = new CommandOutcome(
            command.commandId(),
            receipt.correlationId(),
            command.orderExternalId(),
            fulfillmentExternalId,
            CommandStatus.COMPLETED,
            attempt,
            false,
            "fulfillment completed");
        idempotencyStore.recordOutcome(outcome);
        workflowJournal.append(context, "completed", attempt, CommandStatus.COMPLETED, fulfillmentExternalId);
        return outcome;
      } catch (TransientGatewayException failure) {
        if (retryPolicy.shouldRetry(attempt, failure)) {
          workflowJournal.append(context, "retrying", attempt, CommandStatus.IN_PROGRESS, failure.getMessage());
          continue;
        }
        CommandOutcome outcome = new CommandOutcome(
            command.commandId(),
            receipt.correlationId(),
            command.orderExternalId(),
            command.fulfillmentExternalId(),
            CommandStatus.FAILED,
            attempt,
            false,
            failure.getMessage());
        idempotencyStore.recordOutcome(outcome);
        workflowJournal.append(context, "failed", attempt, CommandStatus.FAILED, failure.getMessage());
        return outcome;
      }
    }

    throw new IllegalStateException("Processing loop exited without a terminal outcome.");
  }

  private String nextCorrelationId() {
    correlationSequence += 1;
    return "wf-2026-04-01-%03d".formatted(correlationSequence);
  }
}
