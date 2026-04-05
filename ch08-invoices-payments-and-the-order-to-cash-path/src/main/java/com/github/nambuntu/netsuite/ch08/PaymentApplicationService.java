package com.github.nambuntu.netsuite.ch08;

import java.util.List;
import java.util.Objects;

public final class PaymentApplicationService {

  private final OrderToCashStore store;

  public PaymentApplicationService(OrderToCashStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public PaymentApplicationResult applyCustomerPayment(ApplyCustomerPaymentCommand command) {
    store.assertPaymentExternalIdAvailable(command.paymentExternalId());
    Invoice invoice = store.requireInvoice(command.invoiceExternalId());
    if (!invoice.customerExternalId().equals(command.customerExternalId())) {
      throw new IllegalArgumentException("payment customer does not match invoice customer");
    }
    if (!invoice.currencyCode().equals(command.currencyCode())) {
      throw new IllegalArgumentException("payment currency does not match invoice currency");
    }

    Invoice updatedInvoice = store.applyPayment(command.invoiceExternalId(), command.amount());
    CustomerPayment payment = new CustomerPayment(
        command.paymentExternalId(),
        command.customerExternalId(),
        command.amount(),
        command.currencyCode(),
        List.of(new PaymentApplication(command.invoiceExternalId(), command.amount())));
    store.savePayment(payment);
    return new PaymentApplicationResult(payment, updatedInvoice);
  }

  public record PaymentApplicationResult(CustomerPayment payment, Invoice invoice) {

    public PaymentApplicationResult {
      Objects.requireNonNull(payment, "payment");
      Objects.requireNonNull(invoice, "invoice");
    }
  }
}
