package com.github.nambuntu.netsuite.ch14;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch14.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch14.model.CaseEventType;
import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.model.CaseStatus;
import com.github.nambuntu.netsuite.ch14.query.SupportQueries;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SupportQueriesTest {

  @Test
  void caseHistoryQueryMapsNormalizedRowKeysIntoHistoryEntry() {
    CaseHistoryEntry entry = SupportQueries.mapCaseHistoryEntry(new SuiteQlRow(Map.of(
        "EXTERNAL_ID", "case-theo-color-mismatch-history-2",
        "support_case_external_id", "case-theo-color-mismatch",
        "Event_Type", "ASSIGNED",
        "STATUS_AFTER_EVENT", "IN_PROGRESS",
        "AUTHOR", "mira-agent",
        "assigned_to", "jules-ops",
        "VISIBILITY", "INTERNAL",
        "Comment", "Routed to operations for shipment mismatch review",
        "changed_at", Instant.parse("2026-04-01T09:20:00Z"))));

    assertThat(entry).isEqualTo(new CaseHistoryEntry(
        "case-theo-color-mismatch-history-2",
        "case-theo-color-mismatch",
        CaseEventType.ASSIGNED,
        CaseStatus.IN_PROGRESS,
        "mira-agent",
        "jules-ops",
        "INTERNAL",
        "Routed to operations for shipment mismatch review",
        Instant.parse("2026-04-01T09:20:00Z")));
  }
}
