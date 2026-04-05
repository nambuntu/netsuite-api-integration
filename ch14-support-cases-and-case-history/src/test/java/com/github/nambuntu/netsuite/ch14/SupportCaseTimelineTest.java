package com.github.nambuntu.netsuite.ch14;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch14.command.AssignSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CloseSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CreateSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.mock.InMemorySupportCaseStore;
import com.github.nambuntu.netsuite.ch14.mock.MockSupportQueryClient;
import com.github.nambuntu.netsuite.ch14.model.CasePriority;
import com.github.nambuntu.netsuite.ch14.service.SupportCaseService;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class SupportCaseTimelineTest {

  @Test
  void caseHistoryIsOrderedAndReadableAsTimeline() {
    InMemorySupportCaseStore store = new InMemorySupportCaseStore();
    SupportCaseService service = new SupportCaseService(store, new MockSupportQueryClient(store));

    service.openCase(new CreateSupportCaseCommand(
        "case-theo-color-mismatch",
        "customer-theo-tran",
        "so-theo-1001",
        "Wrong color and too-chatty personality pack",
        CasePriority.HIGH,
        "mira-agent",
        "Wrong color and too-chatty personality pack reported",
        Instant.parse("2026-04-01T09:12:00Z")));
    service.assignCase(new AssignSupportCaseCommand(
        "case-theo-color-mismatch",
        "jules-ops",
        "mira-agent",
        "Routed to operations for shipment mismatch review",
        Instant.parse("2026-04-01T09:20:00Z")));
    service.addHistoryEntry(
        "case-theo-color-mismatch",
        "jules-ops",
        "Confirmed the wrong finish and personality pack mismatch",
        "2026-04-01T09:35:00Z");
    service.closeCase(new CloseSupportCaseCommand(
        "case-theo-color-mismatch",
        "mira-agent",
        "Replacement arranged and customer notified",
        Instant.parse("2026-04-01T10:05:00Z")));

    assertThat(service.findCaseHistory("case-theo-color-mismatch"))
        .extracting(entry -> entry.eventType().name() + "|" + entry.author() + "|" + entry.comment())
        .containsExactly(
            "OPENED|mira-agent|Wrong color and too-chatty personality pack reported",
            "ASSIGNED|mira-agent|Routed to operations for shipment mismatch review",
            "UPDATED|jules-ops|Confirmed the wrong finish and personality pack mismatch",
            "CLOSED|mira-agent|Replacement arranged and customer notified");
  }
}
