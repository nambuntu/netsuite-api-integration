package com.github.nambuntu.netsuite.ch10.mock;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch10.query.FinanceOpsQueries;
import org.junit.jupiter.api.Test;

class MockSuiteQlClientPaginationTest {

  @Test
  void queryPageSupportsOffsetAndLimitBoundaries() {
    MockSuiteQlClient client = new MockSuiteQlClient(new MockNetSuiteStore());

    var firstPage = client.queryPage(FinanceOpsQueries.ORDER_RECONCILIATION_VIEW, java.util.Map.of(), 0, 2);
    var secondPage = client.queryPage(FinanceOpsQueries.ORDER_RECONCILIATION_VIEW, java.util.Map.of(), 2, 2);

    assertThat(firstPage.items()).hasSize(2);
    assertThat(firstPage.hasMore()).isTrue();
    assertThat(firstPage.totalResults()).isEqualTo(3);
    assertThat(firstPage.items()).extracting(item -> item.salesOrderExternalId())
        .containsExactly("so-theo-1001", "so-theo-1002");

    assertThat(secondPage.items()).hasSize(1);
    assertThat(secondPage.hasMore()).isFalse();
    assertThat(secondPage.totalResults()).isEqualTo(3);
    assertThat(secondPage.items()).extracting(item -> item.salesOrderExternalId())
        .containsExactly("so-ava-1003");
  }
}
