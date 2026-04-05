package com.github.nambuntu.netsuite.ch18;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch18.logging.WorkflowLogContext;
import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandReceipt;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import com.github.nambuntu.netsuite.ch18.store.IdempotencyStore;
import org.junit.jupiter.api.Test;

class IdempotencyStoreTest {

  @Test
  void storesAndRetrievesOutcomeByBusinessCommandId() {
    IdempotencyStore store = new IdempotencyStore();
    FulfillmentCommand command = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");
    WorkflowLogContext context = new WorkflowLogContext(
        "wf-2026-04-01-001",
        command.commandId(),
        command.orderExternalId());

    CommandReceipt receipt = store.recordReceipt(command, context);
    CommandOutcome outcome = new CommandOutcome(
        command.commandId(),
        receipt.correlationId(),
        command.orderExternalId(),
        command.fulfillmentExternalId(),
        CommandStatus.COMPLETED,
        2,
        false,
        "fulfillment completed");
    store.recordOutcome(outcome);

    assertThat(store.findReceipt(command.commandId())).contains(receipt);
    assertThat(store.findOutcome(command.commandId())).contains(outcome);
  }

  @Test
  void duplicateReceiptLookupReturnsOriginalSubmissionIdentity() {
    IdempotencyStore store = new IdempotencyStore();
    FulfillmentCommand command = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");
    WorkflowLogContext context = new WorkflowLogContext(
        "wf-2026-04-01-001",
        command.commandId(),
        command.orderExternalId());

    CommandReceipt first = store.recordReceipt(command, context);
    CommandReceipt second = store.recordReceipt(command, context);

    assertThat(second).isEqualTo(first);
  }
}
