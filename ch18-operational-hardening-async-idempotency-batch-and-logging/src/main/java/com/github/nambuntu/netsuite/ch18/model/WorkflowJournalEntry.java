package com.github.nambuntu.netsuite.ch18.model;

import com.github.nambuntu.netsuite.ch18.logging.WorkflowLogContext;
import java.util.Objects;

public record WorkflowJournalEntry(
    WorkflowLogContext context,
    String step,
    int attempt,
    CommandStatus status,
    String message) {

  public WorkflowJournalEntry {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(step, "step");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(message, "message");
  }
}
