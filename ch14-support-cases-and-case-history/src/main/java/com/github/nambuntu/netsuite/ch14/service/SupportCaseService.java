package com.github.nambuntu.netsuite.ch14.service;

import com.github.nambuntu.netsuite.ch14.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch14.command.AssignSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CloseSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CreateSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.mock.InMemorySupportCaseStore;
import com.github.nambuntu.netsuite.ch14.model.CaseEventType;
import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.model.CaseStatus;
import com.github.nambuntu.netsuite.ch14.model.SupportCase;
import com.github.nambuntu.netsuite.ch14.query.SupportQueries;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SupportCaseService {

  private static final String UNASSIGNED = "unassigned";
  private static final String INTERNAL = "INTERNAL";

  private final InMemorySupportCaseStore supportCaseStore;
  private final SuiteQlClient queryClient;

  public SupportCaseService(InMemorySupportCaseStore supportCaseStore, SuiteQlClient queryClient) {
    this.supportCaseStore = Objects.requireNonNull(supportCaseStore, "supportCaseStore");
    this.queryClient = Objects.requireNonNull(queryClient, "queryClient");
  }

  public SupportCase openCase(CreateSupportCaseCommand command) {
    Objects.requireNonNull(command, "command");
    if (supportCaseStore.exists(command.supportCaseExternalId())) {
      throw new IllegalArgumentException("Support case already exists: " + command.supportCaseExternalId());
    }

    SupportCase supportCase = new SupportCase(
        command.supportCaseExternalId(),
        command.customerExternalId(),
        command.salesOrderExternalId(),
        command.subject(),
        CaseStatus.OPEN,
        command.priority(),
        UNASSIGNED,
        command.openedAt(),
        command.openedAt());
    supportCaseStore.upsertCase(supportCase);
    supportCaseStore.appendHistory(newHistoryEntry(
        command.supportCaseExternalId(),
        CaseEventType.OPENED,
        CaseStatus.OPEN,
        command.author(),
        UNASSIGNED,
        command.comment(),
        command.openedAt(),
        1));
    return supportCase;
  }

  public SupportCase assignCase(AssignSupportCaseCommand command) {
    Objects.requireNonNull(command, "command");
    SupportCase current = requireCase(command.supportCaseExternalId());
    SupportCase updated = new SupportCase(
        current.externalId(),
        current.customerExternalId(),
        current.salesOrderExternalId(),
        current.subject(),
        CaseStatus.IN_PROGRESS,
        current.priority(),
        command.assignedTo(),
        current.openedAt(),
        command.changedAt());
    supportCaseStore.upsertCase(updated);
    supportCaseStore.appendHistory(newHistoryEntry(
        current.externalId(),
        CaseEventType.ASSIGNED,
        CaseStatus.IN_PROGRESS,
        command.author(),
        command.assignedTo(),
        command.comment(),
        command.changedAt(),
        nextHistoryIndex(current.externalId())));
    return updated;
  }

  public SupportCase addHistoryEntry(String supportCaseExternalId, String author, String comment, String changedAt) {
    SupportCase current = requireCase(supportCaseExternalId);
    Instant changedAtInstant = Instant.parse(changedAt);
    SupportCase updated = new SupportCase(
        current.externalId(),
        current.customerExternalId(),
        current.salesOrderExternalId(),
        current.subject(),
        current.status(),
        current.priority(),
        current.assignedTo(),
        current.openedAt(),
        changedAtInstant);
    supportCaseStore.upsertCase(updated);
    supportCaseStore.appendHistory(newHistoryEntry(
        current.externalId(),
        CaseEventType.UPDATED,
        current.status(),
        author,
        current.assignedTo(),
        comment,
        changedAtInstant,
        nextHistoryIndex(current.externalId())));
    return updated;
  }

  public SupportCase closeCase(CloseSupportCaseCommand command) {
    Objects.requireNonNull(command, "command");
    SupportCase current = requireCase(command.supportCaseExternalId());
    SupportCase updated = new SupportCase(
        current.externalId(),
        current.customerExternalId(),
        current.salesOrderExternalId(),
        current.subject(),
        CaseStatus.CLOSED,
        current.priority(),
        current.assignedTo(),
        current.openedAt(),
        command.changedAt());
    supportCaseStore.upsertCase(updated);
    supportCaseStore.appendHistory(newHistoryEntry(
        current.externalId(),
        CaseEventType.CLOSED,
        CaseStatus.CLOSED,
        command.author(),
        current.assignedTo(),
        command.resolutionComment(),
        command.changedAt(),
        nextHistoryIndex(current.externalId())));
    return updated;
  }

  public List<SupportCase> findOpenCases() {
    return supportCaseStore.findOpenCases();
  }

  public List<CaseHistoryEntry> findCaseHistory(String supportCaseExternalId) {
    return queryClient.query(
        SupportQueries.CASE_HISTORY_BY_CASE,
        Map.of("supportCaseExternalId", supportCaseExternalId));
  }

  private SupportCase requireCase(String supportCaseExternalId) {
    return supportCaseStore.findByExternalId(supportCaseExternalId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown support case: " + supportCaseExternalId));
  }

  private int nextHistoryIndex(String supportCaseExternalId) {
    return supportCaseStore.findHistory(supportCaseExternalId).size() + 1;
  }

  private CaseHistoryEntry newHistoryEntry(
      String supportCaseExternalId,
      CaseEventType eventType,
      CaseStatus statusAfterEvent,
      String author,
      String assignedTo,
      String comment,
      Instant changedAt,
      int historyIndex) {
    return new CaseHistoryEntry(
        supportCaseExternalId + "-history-" + historyIndex,
        supportCaseExternalId,
        eventType,
        statusAfterEvent,
        author,
        assignedTo,
        INTERNAL,
        comment,
        changedAt);
  }
}
