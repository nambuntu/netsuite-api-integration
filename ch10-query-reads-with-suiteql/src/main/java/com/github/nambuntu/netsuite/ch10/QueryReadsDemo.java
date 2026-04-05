package com.github.nambuntu.netsuite.ch10;

import com.github.nambuntu.netsuite.ch10.mock.MockNetSuiteStore;
import com.github.nambuntu.netsuite.ch10.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch10.model.OrderReconciliationRow;
import com.github.nambuntu.netsuite.ch10.service.ReconciliationReportService;
import java.math.BigDecimal;
import java.util.List;

public final class QueryReadsDemo {

  private QueryReadsDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    ReconciliationReportService reportService =
        new ReconciliationReportService(new MockSuiteQlClient(new MockNetSuiteStore()));
    StringBuilder builder = new StringBuilder();
    appendSection(builder, "Full reconciliation report", reportService.fullReport());
    builder.append(System.lineSeparator()).append(System.lineSeparator());
    appendSection(builder, "Attention report", reportService.attentionReport());
    return builder.toString();
  }

  private static void appendSection(StringBuilder builder, String heading, List<OrderReconciliationRow> rows) {
    builder.append(heading).append(System.lineSeparator());
    builder.append(String.format(
        "%-19s %-12s %-13s %-8s  %-7s %-7s %-8s %-12s %s%n",
        "customer",
        "order",
        "invoice",
        "invoiced",
        "paid",
        "due",
        "payment",
        "fulfillment",
        "reconciliation"));
    for (OrderReconciliationRow row : rows) {
      builder.append(String.format(
          "%-19s %-12s %-13s %-8s  %-7s %-7s %-8s %-12s %s%n",
          row.customerExternalId(),
          row.salesOrderExternalId(),
          row.invoiceExternalId(),
          money(row.invoiceTotal()),
          money(row.amountPaid()),
          money(row.amountDue()),
          row.paymentState(),
          row.fulfillmentState(),
          row.reconciliationState()));
    }
    builder.setLength(builder.length() - System.lineSeparator().length());
  }

  private static String money(BigDecimal amount) {
    return amount.setScale(2).toPlainString();
  }
}
