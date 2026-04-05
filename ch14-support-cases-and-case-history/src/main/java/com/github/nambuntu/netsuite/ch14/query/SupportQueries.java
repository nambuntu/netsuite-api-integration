package com.github.nambuntu.netsuite.ch14.query;

import com.github.nambuntu.netsuite.ch14.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch14.model.CaseEventType;
import com.github.nambuntu.netsuite.ch14.model.CaseHistoryEntry;
import com.github.nambuntu.netsuite.ch14.model.CaseStatus;

public final class SupportQueries {

  public static final SuiteQlQuery<CaseHistoryEntry> CASE_HISTORY_BY_CASE = new SuiteQlQuery<>(
      "support.caseHistoryByCase",
      """
      SELECT
        history.externalid AS external_id,
        history.supportcaseexternalid AS support_case_external_id,
        history.eventtype AS event_type,
        history.statusafterevent AS status_after_event,
        history.author AS author,
        history.assignedto AS assigned_to,
        history.visibility AS visibility,
        history.comment AS comment,
        history.changedat AS changed_at
      FROM support_case_history
      WHERE history.supportcaseexternalid = :supportCaseExternalId
      ORDER BY history.changedat
      """,
      SupportQueries::mapCaseHistoryEntry);

  private SupportQueries() {
  }

  public static CaseHistoryEntry mapCaseHistoryEntry(SuiteQlRow row) {
    return new CaseHistoryEntry(
        row.string("external_id"),
        row.string("support_case_external_id"),
        CaseEventType.valueOf(row.string("event_type")),
        CaseStatus.valueOf(row.string("status_after_event")),
        row.string("author"),
        row.string("assigned_to"),
        row.string("visibility"),
        row.string("comment"),
        row.instant("changed_at"));
  }
}
