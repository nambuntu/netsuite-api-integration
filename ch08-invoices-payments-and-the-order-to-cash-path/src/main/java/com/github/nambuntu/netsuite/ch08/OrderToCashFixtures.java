package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.List;

public final class OrderToCashFixtures {

  public SalesOrder theoSalesOrder() {
    return new SalesOrder(
        "so-theo-1001",
        "customer-theo-tran",
        new BigDecimal("289.00"),
        "EUR",
        List.of(
            "Nimbus Cat, midnight blue",
            "Personality Pack: Quiet Evenings",
            "Apartment Accessory Bundle"));
  }

  public CreateInvoiceFromSalesOrderCommand createTheoInvoiceCommand() {
    return new CreateInvoiceFromSalesOrderCommand("inv-theo-1001", "so-theo-1001");
  }

  public ApplyCustomerPaymentCommand firstPaymentCommand() {
    return new ApplyCustomerPaymentCommand(
        "pay-theo-1001-part1",
        "customer-theo-tran",
        "inv-theo-1001",
        new BigDecimal("100.00"),
        "EUR");
  }

  public ApplyCustomerPaymentCommand finalPaymentCommand() {
    return new ApplyCustomerPaymentCommand(
        "pay-theo-1001-part2",
        "customer-theo-tran",
        "inv-theo-1001",
        new BigDecimal("189.00"),
        "EUR");
  }

  public SalesOrder requireSalesOrder(String salesOrderExternalId) {
    SalesOrder salesOrder = theoSalesOrder();
    if (!salesOrder.externalId().equals(salesOrderExternalId)) {
      throw new IllegalArgumentException("unknown sales order external ID: " + salesOrderExternalId);
    }
    return salesOrder;
  }
}
