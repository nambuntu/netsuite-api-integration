package com.github.nambuntu.netsuite.ch08;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReceivablesQueryTest {

  @Test
  void openInvoiceQueryReturnsTheoInvoiceAfterPartialPaymentAndNoneAfterFinalPayment() {
    OrderToCashFixtures fixtures = new OrderToCashFixtures();
    OrderToCashStore store = new OrderToCashStore();
    InvoiceWorkflowService invoiceWorkflowService = new InvoiceWorkflowService(fixtures, store);
    PaymentApplicationService paymentApplicationService = new PaymentApplicationService(store);
    ReceivablesQueryService receivablesQueryService = new ReceivablesQueryService(store);

    invoiceWorkflowService.createInvoiceFromSalesOrder(fixtures.createTheoInvoiceCommand());
    paymentApplicationService.applyCustomerPayment(fixtures.firstPaymentCommand());

    List<OpenInvoiceBalance> openInvoicesAfterPartial =
        receivablesQueryService.findOpenInvoicesForCustomer("customer-theo-tran");
    assertThat(openInvoicesAfterPartial).hasSize(1);
    assertThat(openInvoicesAfterPartial.get(0).invoiceExternalId()).isEqualTo("inv-theo-1001");
    assertThat(openInvoicesAfterPartial.get(0).amountDue()).isEqualByComparingTo("189.00");
    assertThat(openInvoicesAfterPartial.get(0).status()).isEqualTo(InvoiceStatus.PARTIALLY_PAID);

    paymentApplicationService.applyCustomerPayment(fixtures.finalPaymentCommand());

    assertThat(receivablesQueryService.findOpenInvoicesForCustomer("customer-theo-tran")).isEmpty();
  }
}
