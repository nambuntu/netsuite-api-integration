package com.github.nambuntu.netsuite.ch19.model;

import java.util.List;
import java.util.Objects;

public record LifecycleEvent(LifecycleStage stage, String summaryLine, List<String> linkedIds) {

  public LifecycleEvent {
    Objects.requireNonNull(stage, "stage");
    Objects.requireNonNull(summaryLine, "summaryLine");
    linkedIds = List.copyOf(Objects.requireNonNull(linkedIds, "linkedIds"));
  }
}
