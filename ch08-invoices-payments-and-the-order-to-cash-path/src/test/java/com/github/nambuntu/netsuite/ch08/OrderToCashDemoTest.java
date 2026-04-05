package com.github.nambuntu.netsuite.ch08;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderToCashDemoTest {

  @Test
  void renderDemoShowsInvoiceCreationPaymentsAndReceivablesQuery() {
    assertThat(OrderToCashDemo.renderDemo()).isEqualTo("""
        Created invoice inv-theo-1001 from sales order so-theo-1001
        Initial amount due for customer-theo-tran: 289.00 EUR
        
        Applied payment pay-theo-1001-part1 for 100.00 EUR
        Invoice inv-theo-1001 status: PARTIALLY_PAID
        Remaining amount due: 189.00 EUR
        
        Open invoices for customer-theo-tran:
        - inv-theo-1001 | createdFrom=so-theo-1001 | amountDue=189.00 EUR | status=PARTIALLY_PAID
        
        Applied payment pay-theo-1001-part2 for 189.00 EUR
        Invoice inv-theo-1001 status: PAID
        Remaining amount due: 0.00 EUR
        
        Open invoices for customer-theo-tran:
        - none""");
  }
}
