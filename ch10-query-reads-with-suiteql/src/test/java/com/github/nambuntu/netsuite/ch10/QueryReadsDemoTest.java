package com.github.nambuntu.netsuite.ch10;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QueryReadsDemoTest {

  @Test
  void renderDemoShowsFullAndAttentionReports() {
    assertThat(QueryReadsDemo.renderDemo()).isEqualTo("""
        Full reconciliation report
        customer            order        invoice       invoiced  paid    due     payment  fulfillment  reconciliation
        customer-theo-tran  so-theo-1001 inv-theo-1001 699.00    300.00  399.00  PARTIAL  SHIPPED      OPEN_BALANCE
        customer-theo-tran  so-theo-1002 inv-theo-1002 49.00     49.00   0.00    PAID     PENDING      FULFILLMENT_LAG
        customer-ava-cole   so-ava-1003  inv-ava-1003  149.00    149.00  0.00    PAID     SHIPPED      SETTLED
        
        Attention report
        customer            order        invoice       invoiced  paid    due     payment  fulfillment  reconciliation
        customer-theo-tran  so-theo-1001 inv-theo-1001 699.00    300.00  399.00  PARTIAL  SHIPPED      OPEN_BALANCE
        customer-theo-tran  so-theo-1002 inv-theo-1002 49.00     49.00   0.00    PAID     PENDING      FULFILLMENT_LAG""");
  }
}
