package com.github.nambuntu.netsuite.ch14.mock;

import com.github.nambuntu.netsuite.ch14.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch14.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.query.SupportQueries;
import com.github.nambuntu.netsuite.ch14.query.SuiteQlQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MockSupportQueryClient implements SuiteQlClient {

  private final InMemorySupportCaseStore supportCaseStore;

  public MockSupportQueryClient(InMemorySupportCaseStore supportCaseStore) {
    this.supportCaseStore = Objects.requireNonNull(supportCaseStore, "supportCaseStore");
  }

  @Override
  public <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params) {
    if (SupportQueries.CASE_HISTORY_BY_CASE.id().equals(query.id())) {
      String supportCaseExternalId = Objects.toString(params.get("supportCaseExternalId"));
      return supportCaseStore.findHistory(supportCaseExternalId).stream()
          .map(this::toRow)
          .map(query::mapRow)
          .toList();
    }
    throw new IllegalArgumentException("Unsupported query id: " + query.id());
  }

  private SuiteQlRow toRow(CaseHistoryEntry entry) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("external_id", entry.externalId());
    row.put("support_case_external_id", entry.supportCaseExternalId());
    row.put("event_type", entry.eventType().name());
    row.put("status_after_event", entry.statusAfterEvent().name());
    row.put("author", entry.author());
    row.put("assigned_to", entry.assignedTo());
    row.put("visibility", entry.visibility());
    row.put("comment", entry.comment());
    row.put("changed_at", entry.changedAt());
    return new SuiteQlRow(row);
  }
}
