package com.github.nambuntu.netsuite.ch18;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandReceipt;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import com.github.nambuntu.netsuite.ch18.mock.InMemoryFulfillmentGateway;
import com.github.nambuntu.netsuite.ch18.policy.BoundedRetryPolicy;
import com.github.nambuntu.netsuite.ch18.service.QueuedCommandProcessor;
import com.github.nambuntu.netsuite.ch18.service.WorkflowJournal;
import com.github.nambuntu.netsuite.ch18.store.IdempotencyStore;
import org.junit.jupiter.api.Test;

class QueuedCommandProcessorTest {

  @Test
  void transientFailureRetriesOnceAndCompletesWithinBounds() {
    InMemoryFulfillmentGateway gateway = new InMemoryFulfillmentGateway();
    gateway.configureFailTimes("fulfill-order:so-theo-1001:shipment-1", 1);
    WorkflowJournal journal = new WorkflowJournal();
    QueuedCommandProcessor processor = new QueuedCommandProcessor(
        new IdempotencyStore(),
        new BoundedRetryPolicy(2),
        journal,
        gateway);
    FulfillmentCommand command = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");

    CommandReceipt receipt = processor.submit(command);
    CommandOutcome outcome = processor.process(command);

    assertThat(outcome.status()).isEqualTo(CommandStatus.COMPLETED);
    assertThat(outcome.attempts()).isEqualTo(2);
    assertThat(outcome.correlationId()).isEqualTo(receipt.correlationId());
    assertThat(gateway.executionCount(command.commandId())).isEqualTo(2);
    assertThat(journal.renderTimeline(command.commandId()))
        .isEqualTo("submitted -> started#1 -> retrying#1 -> started#2 -> completed#2");
  }

  @Test
  void duplicateSubmissionDoesNotExecuteGatewayTwiceAfterCompletion() {
    InMemoryFulfillmentGateway gateway = new InMemoryFulfillmentGateway();
    gateway.configureFailTimes("fulfill-order:so-theo-1001:shipment-1", 1);
    QueuedCommandProcessor processor = new QueuedCommandProcessor(
        new IdempotencyStore(),
        new BoundedRetryPolicy(2),
        new WorkflowJournal(),
        gateway);
    FulfillmentCommand command = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");

    CommandReceipt firstReceipt = processor.submit(command);
    CommandOutcome completed = processor.process(command);
    CommandReceipt duplicateReceipt = processor.submit(command);
    CommandOutcome duplicate = processor.process(command);

    assertThat(duplicateReceipt.correlationId()).isEqualTo(firstReceipt.correlationId());
    assertThat(duplicate.status()).isEqualTo(CommandStatus.ALREADY_COMPLETED);
    assertThat(duplicate.deduplicated()).isTrue();
    assertThat(duplicate.attempts()).isEqualTo(completed.attempts());
    assertThat(gateway.executionCount(command.commandId())).isEqualTo(2);
  }

  @Test
  void retryExhaustionStopsAtConfiguredLimitAndRecordsFailure() {
    InMemoryFulfillmentGateway gateway = new InMemoryFulfillmentGateway();
    gateway.configureFailTimes("fulfill-order:so-theo-1003:shipment-1", 2);
    WorkflowJournal journal = new WorkflowJournal();
    QueuedCommandProcessor processor = new QueuedCommandProcessor(
        new IdempotencyStore(),
        new BoundedRetryPolicy(2),
        journal,
        gateway);
    FulfillmentCommand command = new FulfillmentCommand(
        "fulfill-order:so-theo-1003:shipment-1",
        "so-theo-1003",
        "if-theo-1003-r1");

    processor.submit(command);
    CommandOutcome outcome = processor.process(command);

    assertThat(outcome.status()).isEqualTo(CommandStatus.FAILED);
    assertThat(outcome.attempts()).isEqualTo(2);
    assertThat(gateway.executionCount(command.commandId())).isEqualTo(2);
    assertThat(journal.renderTimeline(command.commandId()))
        .isEqualTo("submitted -> started#1 -> retrying#1 -> started#2 -> failed#2");
  }
}
