package com.github.nambuntu.netsuite.ch18;

import com.github.nambuntu.netsuite.ch18.model.BatchOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandReceipt;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import com.github.nambuntu.netsuite.ch18.mock.InMemoryFulfillmentGateway;
import com.github.nambuntu.netsuite.ch18.policy.BoundedRetryPolicy;
import com.github.nambuntu.netsuite.ch18.service.BatchOperationPlanner;
import com.github.nambuntu.netsuite.ch18.service.QueuedCommandProcessor;
import com.github.nambuntu.netsuite.ch18.service.WorkflowJournal;
import com.github.nambuntu.netsuite.ch18.store.IdempotencyStore;
import java.util.List;
import java.util.StringJoiner;

public final class OperationalHardeningDemo {

  private OperationalHardeningDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  static String renderDemo() {
    InMemoryFulfillmentGateway gateway = new InMemoryFulfillmentGateway();
    gateway.configureFailTimes("fulfill-order:so-theo-1001:shipment-1", 1);

    WorkflowJournal journal = new WorkflowJournal();
    QueuedCommandProcessor processor = new QueuedCommandProcessor(
        new IdempotencyStore(),
        new BoundedRetryPolicy(2),
        journal,
        gateway);
    BatchOperationPlanner batchPlanner = new BatchOperationPlanner(processor);

    FulfillmentCommand mainCommand = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");
    FulfillmentCommand secondBatchCommand = new FulfillmentCommand(
        "fulfill-order:so-theo-1002:shipment-1",
        "so-theo-1002",
        "if-theo-1002-r1");

    CommandReceipt receipt = processor.submit(mainCommand);
    CommandOutcome completedOutcome = processor.process(mainCommand);
    CommandOutcome replayOutcome = processor.process(mainCommand);
    String journalSummary = journal.renderTimeline(mainCommand.commandId());
    List<BatchOutcome> batchOutcomes = batchPlanner.processBatch(List.of(mainCommand, secondBatchCommand), 2);

    StringJoiner joiner = new StringJoiner(System.lineSeparator());
    joiner.add("correlation=" + receipt.correlationId());
    joiner.add("command=" + mainCommand.commandId());
    joiner.add("attempts=" + completedOutcome.attempts());
    joiner.add("deduplicated=" + (replayOutcome.deduplicated() ? "yes" : "no"));
    joiner.add("final-status=" + completedOutcome.status().rendered());
    joiner.add("replay-status=" + replayOutcome.status().rendered());
    BatchOutcome batchOutcome = batchOutcomes.get(0);
    joiner.add("batch-chunk=" + batchOutcome.chunkLabel());
    batchOutcome.itemOutcomes().forEach(outcome ->
        joiner.add("batch-item=" + outcome.commandId() + " status=" + outcome.status().rendered()));
    joiner.add("journal=" + journalSummary);
    return joiner.toString();
  }
}
