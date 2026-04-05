package com.github.nambuntu.netsuite.ch05;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LifecycleTimeline {

  private final List<LifecycleEvent> events = new ArrayList<>();
  private final Set<String> recordedKeys = new LinkedHashSet<>();

  public boolean record(LifecyclePhase phase, String primaryExternalId, String description) {
    LifecycleEvent event = new LifecycleEvent(phase, primaryExternalId, description);
    String key = event.phase().name() + "::" + event.primaryExternalId();
    if (!recordedKeys.add(key)) {
      return false;
    }
    events.add(event);
    return true;
  }

  public List<LifecycleEvent> events() {
    return List.copyOf(events);
  }

  public List<String> summaryLines() {
    return events.stream()
        .map(LifecycleEvent::summaryLine)
        .toList();
  }

  public String renderSummary() {
    return String.join(System.lineSeparator(), summaryLines());
  }
}
