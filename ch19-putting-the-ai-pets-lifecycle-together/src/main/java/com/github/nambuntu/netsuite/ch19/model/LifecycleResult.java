package com.github.nambuntu.netsuite.ch19.model;

import java.util.List;
import java.util.Objects;

public record LifecycleResult(LifecycleScenario scenario, List<LifecycleEvent> events) {

  public LifecycleResult {
    Objects.requireNonNull(scenario, "scenario");
    events = List.copyOf(Objects.requireNonNull(events, "events"));
  }
}
