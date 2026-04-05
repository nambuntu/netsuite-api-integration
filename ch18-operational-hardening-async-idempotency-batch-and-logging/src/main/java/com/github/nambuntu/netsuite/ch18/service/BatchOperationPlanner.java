package com.github.nambuntu.netsuite.ch18.service;

import com.github.nambuntu.netsuite.ch18.model.BatchOutcome;
import com.github.nambuntu.netsuite.ch18.model.CommandOutcome;
import com.github.nambuntu.netsuite.ch18.model.FulfillmentCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BatchOperationPlanner {

  private final QueuedCommandProcessor queuedCommandProcessor;

  public BatchOperationPlanner(QueuedCommandProcessor queuedCommandProcessor) {
    this.queuedCommandProcessor = Objects.requireNonNull(queuedCommandProcessor, "queuedCommandProcessor");
  }

  public List<List<FulfillmentCommand>> chunk(List<FulfillmentCommand> commands, int chunkSize) {
    Objects.requireNonNull(commands, "commands");
    if (chunkSize < 1) {
      throw new IllegalArgumentException("chunkSize must be at least 1");
    }
    List<List<FulfillmentCommand>> chunks = new ArrayList<>();
    for (int index = 0; index < commands.size(); index += chunkSize) {
      int endIndex = Math.min(index + chunkSize, commands.size());
      chunks.add(List.copyOf(commands.subList(index, endIndex)));
    }
    return List.copyOf(chunks);
  }

  public List<BatchOutcome> processBatch(List<FulfillmentCommand> commands, int chunkSize) {
    List<List<FulfillmentCommand>> chunks = chunk(commands, chunkSize);
    List<BatchOutcome> outcomes = new ArrayList<>();
    for (int index = 0; index < chunks.size(); index++) {
      List<CommandOutcome> itemOutcomes = new ArrayList<>();
      for (FulfillmentCommand command : chunks.get(index)) {
        queuedCommandProcessor.submit(command);
        itemOutcomes.add(queuedCommandProcessor.process(command));
      }
      outcomes.add(new BatchOutcome(index + 1, chunks.size(), itemOutcomes));
    }
    return List.copyOf(outcomes);
  }
}
