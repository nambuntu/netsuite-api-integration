package com.github.nambuntu.netsuite.ch14.mock;

import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.model.CaseStatus;
import com.github.nambuntu.netsuite.ch14.model.SupportCase;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemorySupportCaseStore {

  private final Map<String, SupportCase> supportCases = new LinkedHashMap<>();
  private final Map<String, List<CaseHistoryEntry>> historyEntries = new LinkedHashMap<>();

  public boolean exists(String supportCaseExternalId) {
    return supportCases.containsKey(supportCaseExternalId);
  }

  public void upsertCase(SupportCase supportCase) {
    supportCases.put(supportCase.externalId(), supportCase);
  }

  public Optional<SupportCase> findByExternalId(String supportCaseExternalId) {
    return Optional.ofNullable(supportCases.get(supportCaseExternalId));
  }

  public List<SupportCase> findOpenCases() {
    return supportCases.values().stream()
        .filter(supportCase -> supportCase.status() != CaseStatus.CLOSED)
        .sorted(Comparator.comparing(SupportCase::externalId))
        .toList();
  }

  public void appendHistory(CaseHistoryEntry entry) {
    historyEntries.computeIfAbsent(entry.supportCaseExternalId(), ignored -> new ArrayList<>()).add(entry);
  }

  public List<CaseHistoryEntry> findHistory(String supportCaseExternalId) {
    return historyEntries.getOrDefault(supportCaseExternalId, List.of()).stream()
        .sorted(Comparator.comparing(CaseHistoryEntry::changedAt).thenComparing(CaseHistoryEntry::externalId))
        .toList();
  }
}
