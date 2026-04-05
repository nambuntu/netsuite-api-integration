package com.github.nambuntu.netsuite.ch18.service;

import com.github.nambuntu.netsuite.ch18.logging.WorkflowLogContext;
import com.github.nambuntu.netsuite.ch18.model.CommandStatus;
import com.github.nambuntu.netsuite.ch18.model.WorkflowJournalEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class WorkflowJournal {

  private final List<WorkflowJournalEntry> entries = new ArrayList<>();

  public void append(
      WorkflowLogContext context,
      String step,
      int attempt,
      CommandStatus status,
      String message) {
    entries.add(new WorkflowJournalEntry(context, step, attempt, status, message));
  }

  public List<WorkflowJournalEntry> entries() {
    return List.copyOf(entries);
  }

  public List<WorkflowJournalEntry> entriesFor(String commandId) {
    return entries.stream()
        .filter(entry -> entry.context().commandId().equals(commandId))
        .toList();
  }

  public String renderTimeline(String commandId) {
    return entriesFor(commandId).stream()
        .map(entry -> entry.attempt() > 0 ? entry.step() + "#" + entry.attempt() : entry.step())
        .collect(Collectors.joining(" -> "));
  }
}
