package com.github.nambuntu.netsuite.ch05;

public record LifecycleEvent(
    LifecyclePhase phase,
    String primaryExternalId,
    String description) {

  public LifecycleEvent {
    if (phase == null) {
      throw new IllegalArgumentException("phase must not be null");
    }
    primaryExternalId = requireText(primaryExternalId, "primaryExternalId");
    description = requireText(description, "description");
  }

  public String summaryLine() {
    return String.format("%-13s %-27s %s", phase.displayName(), primaryExternalId, description);
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
