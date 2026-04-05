package com.github.nambuntu.netsuite.ch07;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SalesOrderDemoTest {

  @Test
  void renderDemoShowsOrderIdentityStatusAndReadableLines() {
    assertThat(SalesOrderDemo.renderDemo()).isEqualTo("""
        Created sales order so-theo-1001
        Customer: customer-theo-tran
        Status: PENDING_FULFILLMENT
        Lines:
        - Nimbus Cat, midnight blue x1
        - Personality Pack: Quiet Evenings x1
        - Apartment Accessory Bundle x1
        Total lines: 3""");
  }
}
