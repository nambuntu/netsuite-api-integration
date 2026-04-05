package com.github.nambuntu.netsuite.ch08;

import java.util.List;

public final class OrderToCashDemo {

  private OrderToCashDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    OrderToCashFixtures fixtures = new OrderToCashFixtures();
    OrderToCashStore store = new OrderToCashStore();
    InvoiceWorkflowService invoiceWorkflowService = new InvoiceWorkflowService(fixtures, store);
    PaymentApplicationService paymentApplicationService = new PaymentApplicationService(store);
    ReceivablesQueryService receivablesQueryService = new ReceivablesQueryService(store);

    StringBuilder builder = new StringBuilder();

    Invoice createdInvoice =
        invoiceWorkflowService.createInvoiceFromSalesOrder(fixtures.createTheoInvoiceCommand());
    builder.append("Created invoice ")
        .append(createdInvoice.externalId())
        .append(" from sales order ")
        .append(createdInvoice.createdFromSalesOrderExternalId())
        .append(System.lineSeparator());
    builder.append("Initial amount due for ")
        .append(createdInvoice.customerExternalId())
        .append(": ")
        .append(Money.format(createdInvoice.amountDue(), createdInvoice.currencyCode()))
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    PaymentApplicationService.PaymentApplicationResult firstPayment =
        paymentApplicationService.applyCustomerPayment(fixtures.firstPaymentCommand());
    builder.append("Applied payment ")
        .append(firstPayment.payment().externalId())
        .append(" for ")
        .append(Money.format(firstPayment.payment().amount(), firstPayment.payment().currencyCode()))
        .append(System.lineSeparator());
    builder.append("Invoice ")
        .append(firstPayment.invoice().externalId())
        .append(" status: ")
        .append(firstPayment.invoice().status())
        .append(System.lineSeparator());
    builder.append("Remaining amount due: ")
        .append(Money.format(firstPayment.invoice().amountDue(), firstPayment.invoice().currencyCode()))
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    appendOpenInvoices(builder, firstPayment.invoice().customerExternalId(),
        receivablesQueryService.findOpenInvoicesForCustomer(firstPayment.invoice().customerExternalId()));
    builder.append(System.lineSeparator());

    PaymentApplicationService.PaymentApplicationResult finalPayment =
        paymentApplicationService.applyCustomerPayment(fixtures.finalPaymentCommand());
    builder.append("Applied payment ")
        .append(finalPayment.payment().externalId())
        .append(" for ")
        .append(Money.format(finalPayment.payment().amount(), finalPayment.payment().currencyCode()))
        .append(System.lineSeparator());
    builder.append("Invoice ")
        .append(finalPayment.invoice().externalId())
        .append(" status: ")
        .append(finalPayment.invoice().status())
        .append(System.lineSeparator());
    builder.append("Remaining amount due: ")
        .append(Money.format(finalPayment.invoice().amountDue(), finalPayment.invoice().currencyCode()))
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    appendOpenInvoices(builder, finalPayment.invoice().customerExternalId(),
        receivablesQueryService.findOpenInvoicesForCustomer(finalPayment.invoice().customerExternalId()));
    return builder.toString();
  }

  private static void appendOpenInvoices(
      StringBuilder builder,
      String customerExternalId,
      List<OpenInvoiceBalance> openInvoices) {

    builder.append("Open invoices for ")
        .append(customerExternalId)
        .append(":")
        .append(System.lineSeparator());
    if (openInvoices.isEmpty()) {
      builder.append("- none");
      return;
    }
    for (OpenInvoiceBalance openInvoice : openInvoices) {
      builder.append(openInvoice.summaryLine()).append(System.lineSeparator());
    }
  }
}
