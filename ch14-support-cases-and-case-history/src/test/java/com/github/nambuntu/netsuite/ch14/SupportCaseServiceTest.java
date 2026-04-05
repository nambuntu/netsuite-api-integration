package com.github.nambuntu.netsuite.ch14;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch14.command.AssignSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CloseSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.command.CreateSupportCaseCommand;
import com.github.nambuntu.netsuite.ch14.mock.InMemorySupportCaseStore;
import com.github.nambuntu.netsuite.ch14.mock.MockSupportQueryClient;
import com.github.nambuntu.netsuite.ch14.model.CasePriority;
import com.github.nambuntu.netsuite.ch14.model.CaseStatus;
import com.github.nambuntu.netsuite.ch14.service.SupportCaseService;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class SupportCaseServiceTest {

  @Test
  void openingTheoCaseStoresCurrentStateAndReturnsItFromOpenCases() {
    SupportCaseService service = newService();

    service.openCase(new CreateSupportCaseCommand(
        "case-theo-color-mismatch",
        "customer-theo-tran",
        "so-theo-1001",
        "Wrong color and too-chatty personality pack",
        CasePriority.HIGH,
        "mira-agent",
        "Wrong color and too-chatty personality pack reported",
        Instant.parse("2026-04-01T09:12:00Z")));

    assertThat(service.findOpenCases())
        .singleElement()
        .satisfies(supportCase -> {
          assertThat(supportCase.status()).isEqualTo(CaseStatus.OPEN);
          assertThat(supportCase.priority()).isEqualTo(CasePriority.HIGH);
          assertThat(supportCase.customerExternalId()).isEqualTo("customer-theo-tran");
          assertThat(supportCase.salesOrderExternalId()).isEqualTo("so-theo-1001");
        });
  }

  @Test
  void assigningCaseMovesCurrentStateToInProgressAndSetsOwner() {
    SupportCaseService service = newService();
    openTheoCase(service);

    service.assignCase(new AssignSupportCaseCommand(
        "case-theo-color-mismatch",
        "jules-ops",
        "mira-agent",
        "Routed to operations for shipment mismatch review",
        Instant.parse("2026-04-01T09:20:00Z")));

    assertThat(service.findOpenCases())
        .singleElement()
        .satisfies(supportCase -> {
          assertThat(supportCase.status()).isEqualTo(CaseStatus.IN_PROGRESS);
          assertThat(supportCase.assignedTo()).isEqualTo("jules-ops");
        });
  }

  @Test
  void closingCaseRemovesItFromOpenCasesWithoutLosingHistoryContext() {
    SupportCaseService service = newService();
    openTheoCase(service);
    service.assignCase(new AssignSupportCaseCommand(
        "case-theo-color-mismatch",
        "jules-ops",
        "mira-agent",
        "Routed to operations for shipment mismatch review",
        Instant.parse("2026-04-01T09:20:00Z")));

    service.closeCase(new CloseSupportCaseCommand(
        "case-theo-color-mismatch",
        "mira-agent",
        "Replacement arranged and customer notified",
        Instant.parse("2026-04-01T10:05:00Z")));

    assertThat(service.findOpenCases()).isEmpty();
    assertThat(service.findCaseHistory("case-theo-color-mismatch"))
        .extracting(entry -> entry.statusAfterEvent().name() + " " + entry.comment())
        .containsExactly(
            "OPEN Wrong color and too-chatty personality pack reported",
            "IN_PROGRESS Routed to operations for shipment mismatch review",
            "CLOSED Replacement arranged and customer notified");
  }

  private static SupportCaseService newService() {
    InMemorySupportCaseStore store = new InMemorySupportCaseStore();
    return new SupportCaseService(store, new MockSupportQueryClient(store));
  }

  private static void openTheoCase(SupportCaseService service) {
    service.openCase(new CreateSupportCaseCommand(
        "case-theo-color-mismatch",
        "customer-theo-tran",
        "so-theo-1001",
        "Wrong color and too-chatty personality pack",
        CasePriority.HIGH,
        "mira-agent",
        "Wrong color and too-chatty personality pack reported",
        Instant.parse("2026-04-01T09:12:00Z")));
  }
}
