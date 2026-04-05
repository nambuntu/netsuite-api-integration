package com.github.nambuntu.netsuite.ch18;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch18.model.BatchOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import com.github.nambuntu.netsuite.ch18.mock.InMemoryFulfillmentGateway;
import com.github.nambuntu.netsuite.ch18.policy.BoundedRetryPolicy;
import com.github.nambuntu.netsuite.ch18.service.BatchOperationPlanner;
import com.github.nambuntu.netsuite.ch18.service.QueuedCommandProcessor;
import com.github.nambuntu.netsuite.ch18.service.WorkflowJournal;
import com.github.nambuntu.netsuite.ch18.store.IdempotencyStore;
import java.util.List;
import org.junit.jupiter.api.Test;

class BatchOperationPlannerTest {

  @Test
  void twoCommandBatchUsesSingleChunkAndPreservesPerItemOutcomes() {
    InMemoryFulfillmentGateway gateway = new InMemoryFulfillmentGateway();
    gateway.configureFailTimes("fulfill-order:so-theo-1001:shipment-1", 1);
    QueuedCommandProcessor processor = new QueuedCommandProcessor(
        new IdempotencyStore(),
        new BoundedRetryPolicy(2),
        new WorkflowJournal(),
        gateway);
    BatchOperationPlanner planner = new BatchOperationPlanner(processor);

    FulfillmentCommand first = new FulfillmentCommand(
        "fulfill-order:so-theo-1001:shipment-1",
        "so-theo-1001",
        "if-theo-1001-r1");
    FulfillmentCommand second = new FulfillmentCommand(
        "fulfill-order:so-theo-1002:shipment-1",
        "so-theo-1002",
        "if-theo-1002-r1");

    processor.submit(first);
    processor.process(first);
    List<BatchOutcome> outcomes = planner.processBatch(List.of(first, second), 2);

    assertThat(planner.chunk(List.of(first, second), 2)).hasSize(1);
    assertThat(outcomes).hasSize(1);
    assertThat(outcomes.get(0).chunkLabel()).isEqualTo("1/1");
    assertThat(outcomes.get(0).itemOutcomes()).extracting(outcome -> outcome.status())
        .containsExactly(CommandStatus.ALREADY_COMPLETED, CommandStatus.COMPLETED);
  }
}
