package com.github.nambuntu.netsuite.ch07;

public final class SalesOrderSummaryFormatter {

  public String format(SalesOrder salesOrder) {
    StringBuilder builder = new StringBuilder();
    builder.append("Created sales order ").append(salesOrder.externalId()).append(System.lineSeparator());
    builder.append("Customer: ").append(salesOrder.customer().externalId()).append(System.lineSeparator());
    builder.append("Status: ").append(salesOrder.status()).append(System.lineSeparator());
    builder.append("Lines:").append(System.lineSeparator());
    for (SalesOrderLine line : salesOrder.lines()) {
      builder.append("- ")
          .append(line.itemDisplayName())
          .append(" x")
          .append(line.quantity())
          .append(System.lineSeparator());
    }
    builder.append("Total lines: ").append(salesOrder.totalLines());
    return builder.toString();
  }
}
