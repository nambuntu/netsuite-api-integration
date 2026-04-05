package com.github.nambuntu.netsuite.ch10.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch10.mock.MockNetSuiteStore;
import com.github.nambuntu.netsuite.ch10.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch10.model.ReconciliationState;
import org.junit.jupiter.api.Test;

class ReconciliationReportServiceTest {

  private final ReconciliationReportService service =
      new ReconciliationReportService(new MockSuiteQlClient(new MockNetSuiteStore()));

  @Test
  void fullReportReturnsThreeRowsInStableOrderWithExpectedClassifications() {
    assertThat(service.fullReport())
        .extracting(row -> row.salesOrderExternalId() + ":" + row.reconciliationState())
        .containsExactly(
            "so-theo-1001:" + ReconciliationState.OPEN_BALANCE,
            "so-theo-1002:" + ReconciliationState.FULFILLMENT_LAG,
            "so-ava-1003:" + ReconciliationState.SETTLED);
  }

  @Test
  void attentionReportReturnsOnlyRowsNeedingAttention() {
    assertThat(service.attentionReport())
        .extracting(row -> row.salesOrderExternalId() + ":" + row.reconciliationState())
        .containsExactly(
            "so-theo-1001:" + ReconciliationState.OPEN_BALANCE,
            "so-theo-1002:" + ReconciliationState.FULFILLMENT_LAG);
  }
}
