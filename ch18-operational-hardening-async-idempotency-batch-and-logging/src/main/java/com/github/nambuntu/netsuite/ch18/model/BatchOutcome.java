package com.github.nambuntu.netsuite.ch18.model;

import java.util.List;
import java.util.Objects;

public record BatchOutcome(int chunkNumber, int totalChunks, List<CommandOutcome> itemOutcomes) {

  public BatchOutcome {
    itemOutcomes = List.copyOf(Objects.requireNonNull(itemOutcomes, "itemOutcomes"));
  }

  public String chunkLabel() {
    return chunkNumber + "/" + totalChunks;
  }
}
