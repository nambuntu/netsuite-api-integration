package com.github.nambuntu.netsuite.ch08;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvoiceWorkflowTest {

  @Test
  void createInvoiceFromSalesOrderPreservesSourceLinkageAndFullAmountDue() {
    OrderToCashFixtures fixtures = new OrderToCashFixtures();
    OrderToCashStore store = new OrderToCashStore();
    InvoiceWorkflowService service = new InvoiceWorkflowService(fixtures, store);

    Invoice invoice = service.createInvoiceFromSalesOrder(fixtures.createTheoInvoiceCommand());

    assertThat(invoice.externalId()).isEqualTo("inv-theo-1001");
    assertThat(invoice.createdFromSalesOrderExternalId()).isEqualTo("so-theo-1001");
    assertThat(invoice.customerExternalId()).isEqualTo("customer-theo-tran");
    assertThat(invoice.totalAmount()).isEqualByComparingTo("289.00");
    assertThat(invoice.amountDue()).isEqualByComparingTo("289.00");
    assertThat(invoice.status()).isEqualTo(InvoiceStatus.OPEN);
  }
}
