package com.github.nambuntu.netsuite.ch15.store;

import com.github.nambuntu.netsuite.ch15.fixture.ReturnFixtures;
import com.github.nambuntu.netsuite.ch15.model.ExchangeOutcome;
import com.github.nambuntu.netsuite.ch15.model.RefundOutcome;
import com.github.nambuntu.netsuite.ch15.model.ReturnAuthorization;
import com.github.nambuntu.netsuite.ch15.model.ReturnProgressEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryReturnStore {

  private final Map<String, StoredSupportCase> supportCases = new LinkedHashMap<>();
  private final Map<String, ReturnAuthorization> returnAuthorizations = new LinkedHashMap<>();
  private final Map<String, ExchangeOutcome> exchangeOutcomes = new LinkedHashMap<>();
  private final Map<String, RefundOutcome> refundOutcomes = new LinkedHashMap<>();
  private final Map<String, List<ReturnProgressEvent>> timelineBySupportCase = new LinkedHashMap<>();

  public void seedFixture(ReturnFixtures fixture) {
    supportCases.put(
        fixture.supportCaseExternalId(),
        new StoredSupportCase(
            fixture.supportCaseExternalId(),
            fixture.customerExternalId(),
            fixture.originalSalesOrderExternalId(),
            fixture.supportSubject(),
            "OPEN",
            fixture.caseOpenedAt()));
    appendEvent(
        fixture.supportCaseExternalId(),
        "OPENED",
        "Theo reports wrong color and overly chatty personality pack",
        fixture.caseOpenedAt());
  }

  public void saveReturnAuthorization(ReturnAuthorization record) {
    returnAuthorizations.put(record.externalId(), record);
  }

  public Optional<ReturnAuthorization> findReturnAuthorization(String externalId) {
    return Optional.ofNullable(returnAuthorizations.get(externalId));
  }

  public List<ReturnAuthorization> findReturnAuthorizationsBySupportCase(String supportCaseExternalId) {
    return returnAuthorizations.values().stream()
        .filter(record -> record.supportCaseExternalId().equals(supportCaseExternalId))
        .sorted(Comparator.comparing(ReturnAuthorization::authorizedAt))
        .toList();
  }

  public void saveExchangeOutcome(String returnAuthorizationExternalId, ExchangeOutcome outcome) {
    exchangeOutcomes.put(returnAuthorizationExternalId, outcome);
  }

  public Optional<ExchangeOutcome> findExchangeOutcome(String returnAuthorizationExternalId) {
    return Optional.ofNullable(exchangeOutcomes.get(returnAuthorizationExternalId));
  }

  public void saveRefundOutcome(String returnAuthorizationExternalId, RefundOutcome outcome) {
    refundOutcomes.put(returnAuthorizationExternalId, outcome);
  }

  public Optional<RefundOutcome> findRefundOutcome(String returnAuthorizationExternalId) {
    return Optional.ofNullable(refundOutcomes.get(returnAuthorizationExternalId));
  }

  public void closeSupportCase(String supportCaseExternalId, Instant closedAt) {
    StoredSupportCase supportCase = requireSupportCase(supportCaseExternalId);
    supportCases.put(
        supportCaseExternalId,
        new StoredSupportCase(
            supportCase.externalId(),
            supportCase.customerExternalId(),
            supportCase.sourceSalesOrderExternalId(),
            supportCase.subject(),
            "CLOSED",
            closedAt));
  }

  public String supportCaseStatus(String supportCaseExternalId) {
    return requireSupportCase(supportCaseExternalId).status();
  }

  public void appendEvent(String supportCaseExternalId, String eventType, String detail, Instant occurredAt) {
    List<ReturnProgressEvent> events = timelineBySupportCase.computeIfAbsent(
        supportCaseExternalId,
        ignored -> new ArrayList<>());
    events.add(new ReturnProgressEvent(
        supportCaseExternalId,
        events.size() + 1,
        eventType,
        detail,
        occurredAt));
  }

  public List<ReturnProgressEvent> timeline(String supportCaseExternalId) {
    return timelineBySupportCase.getOrDefault(supportCaseExternalId, List.of()).stream()
        .sorted(Comparator.comparing(ReturnProgressEvent::sequenceNumber))
        .toList();
  }

  private StoredSupportCase requireSupportCase(String supportCaseExternalId) {
    StoredSupportCase supportCase = supportCases.get(supportCaseExternalId);
    if (supportCase == null) {
      throw new IllegalArgumentException("Unknown support case: " + supportCaseExternalId);
    }
    return supportCase;
  }

  private record StoredSupportCase(
      String externalId,
      String customerExternalId,
      String sourceSalesOrderExternalId,
      String subject,
      String status,
      Instant lastUpdatedAt) {
  }
}
