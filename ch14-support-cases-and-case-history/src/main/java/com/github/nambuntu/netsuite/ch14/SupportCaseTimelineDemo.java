package com.github.nambuntu.netsuite.ch14;

import com.github.nambuntu.netsuite.ch14.command.AssignSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CloseSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CreateSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.mock.InMemorySupportCaseStore;
import com.github.nambuntu.netsuite.ch14.mock.MockSupportQueryClient;
import com.github.nambuntu.netsuite.ch14.model.CaseEventType;
import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.model.CasePriority;
import com.github.nambuntu.netsuite.ch14.model.SupportCase;
import com.github.nambuntu.netsuite.ch14.service.SupportCaseService;
import java.time.Instant;
import java.util.List;

public final class SupportCaseTimelineDemo {

  private static final String SUPPORT_CASE_EXTERNAL_ID = "case-theo-color-mismatch";

  private SupportCaseTimelineDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    InMemorySupportCaseStore store = new InMemorySupportCaseStore();
    SupportCaseService service = new SupportCaseService(store, new MockSupportQueryClient(store));

    SupportCase opened = service.openCase(new CreateSupportCaseCommand(
        SUPPORT_CASE_EXTERNAL_ID,
        "customer-theo-tran",
        "so-theo-1001",
        "Wrong color and too-chatty personality pack",
        CasePriority.HIGH,
        "mira-agent",
        "Wrong color and too-chatty personality pack reported",
        Instant.parse("2026-04-01T09:12:00Z")));

    List<SupportCase> openCases = service.findOpenCases();
    SupportCase assigned = service.assignCase(new AssignSupportCaseCommand(
        SUPPORT_CASE_EXTERNAL_ID,
        "jules-ops",
        "mira-agent",
        "Routed to operations for shipment mismatch review",
        Instant.parse("2026-04-01T09:20:00Z")));
    service.addHistoryEntry(
        SUPPORT_CASE_EXTERNAL_ID,
        "jules-ops",
        "Confirmed the wrong finish and personality pack mismatch",
        "2026-04-01T09:35:00Z");
    SupportCase closed = service.closeCase(new CloseSupportCaseCommand(
        SUPPORT_CASE_EXTERNAL_ID,
        "mira-agent",
        "Replacement arranged and customer notified",
        Instant.parse("2026-04-01T10:05:00Z")));
    List<CaseHistoryEntry> history = service.findCaseHistory(SUPPORT_CASE_EXTERNAL_ID);

    return renderOutput(opened, openCases, assigned, closed, history);
  }

  private static String renderOutput(
      SupportCase opened,
      List<SupportCase> openCases,
      SupportCase assigned,
      SupportCase closed,
      List<CaseHistoryEntry> history) {
    StringBuilder builder = new StringBuilder();
    builder.append("Opened support case: ")
        .append(opened.externalId())
        .append(" for ")
        .append(opened.customerExternalId())
        .append(" order=")
        .append(opened.salesOrderExternalId())
        .append(System.lineSeparator())
        .append("Open cases:")
        .append(System.lineSeparator());
    for (SupportCase openCase : openCases) {
      builder.append(" - ")
          .append(openCase.externalId())
          .append(" [")
          .append(openCase.status())
          .append("] priority=")
          .append(openCase.priority())
          .append(" assignedTo=")
          .append(openCase.assignedTo())
          .append(System.lineSeparator());
    }
    builder.append(System.lineSeparator())
        .append("Updated support case: ")
        .append(assigned.externalId())
        .append(" assignedTo=")
        .append(assigned.assignedTo())
        .append(" status=")
        .append(assigned.status())
        .append(System.lineSeparator())
        .append("Closed support case: ")
        .append(closed.externalId())
        .append(" status=")
        .append(closed.status())
        .append(System.lineSeparator())
        .append(System.lineSeparator())
        .append("Case timeline:")
        .append(System.lineSeparator());
    for (CaseHistoryEntry entry : history) {
      builder.append(" - ")
          .append(entry.eventType())
          .append(" by ")
          .append(entry.author());
      if (entry.eventType() == CaseEventType.ASSIGNED) {
        builder.append(" -> ").append(entry.assignedTo());
      }
      builder.append(": ")
          .append(entry.comment())
          .append(System.lineSeparator());
    }
    builder.setLength(builder.length() - System.lineSeparator().length());
    return builder.toString();
  }
}
