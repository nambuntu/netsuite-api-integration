package com.github.nambuntu.netsuite.ch11.query;

import com.github.nambuntu.netsuite.ch11.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch11.model.CampaignActivityHistoryRow;
import com.github.nambuntu.netsuite.ch11.model.MarketingActivityKind;

public final class CampaignActivityQueries {

  public static final SuiteQlQuery<CampaignActivityHistoryRow> CAMPAIGN_ACTIVITY_HISTORY = new SuiteQlQuery<>(
      "campaigns.activityHistory",
      """
      SELECT
        activity.occurredat AS occurred_at,
        activity.kind AS activity_kind,
        activity.subjectexternalid AS subject_external_id,
        activity.note AS note
      FROM campaign_activity_history
      WHERE activity.campaignexternalid = :campaignExternalId
      ORDER BY activity.occurredat
      """,
      CampaignActivityQueries::mapHistoryRow);

  private CampaignActivityQueries() {
  }

  public static CampaignActivityHistoryRow mapHistoryRow(SuiteQlRow row) {
    return new CampaignActivityHistoryRow(
        row.instant("occurred_at"),
        MarketingActivityKind.valueOf(row.string("activity_kind")),
        row.string("subject_external_id"),
        row.string("note"));
  }
}
