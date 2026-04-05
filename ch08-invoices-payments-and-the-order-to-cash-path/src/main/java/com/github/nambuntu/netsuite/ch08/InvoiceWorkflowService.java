package com.github.nambuntu.netsuite.ch08;

import java.util.Objects;

public final class InvoiceWorkflowService {

  private final OrderToCashFixtures fixtures;
  private final OrderToCashStore store;

  public InvoiceWorkflowService(OrderToCashFixtures fixtures, OrderToCashStore store) {
    this.fixtures = Objects.requireNonNull(fixtures, "fixtures");
    this.store = Objects.requireNonNull(store, "store");
  }

  public Invoice createInvoiceFromSalesOrder(CreateInvoiceFromSalesOrderCommand command) {
    SalesOrder salesOrder = fixtures.requireSalesOrder(command.salesOrderExternalId());
    Invoice invoice = new Invoice(
        command.invoiceExternalId(),
        salesOrder.customerExternalId(),
        salesOrder.externalId(),
        salesOrder.totalAmount(),
        salesOrder.totalAmount(),
        salesOrder.currencyCode(),
        InvoiceStatus.OPEN);

    store.saveInvoice(invoice);
    return store.requireInvoice(command.invoiceExternalId());
  }
}
