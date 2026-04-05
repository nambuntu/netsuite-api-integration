package com.github.nambuntu.netsuite.ch08;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PaymentApplicationTest {

  @Test
  void partialAndFinalPaymentMoveInvoiceFromOpenToPaid() {
    OrderToCashFixtures fixtures = new OrderToCashFixtures();
    OrderToCashStore store = new OrderToCashStore();
    InvoiceWorkflowService invoiceWorkflowService = new InvoiceWorkflowService(fixtures, store);
    PaymentApplicationService paymentApplicationService = new PaymentApplicationService(store);

    invoiceWorkflowService.createInvoiceFromSalesOrder(fixtures.createTheoInvoiceCommand());

    PaymentApplicationService.PaymentApplicationResult firstPayment =
        paymentApplicationService.applyCustomerPayment(fixtures.firstPaymentCommand());
    assertThat(firstPayment.invoice().amountDue()).isEqualByComparingTo("189.00");
    assertThat(firstPayment.invoice().status()).isEqualTo(InvoiceStatus.PARTIALLY_PAID);

    PaymentApplicationService.PaymentApplicationResult finalPayment =
        paymentApplicationService.applyCustomerPayment(fixtures.finalPaymentCommand());
    assertThat(finalPayment.invoice().amountDue()).isEqualByComparingTo("0.00");
    assertThat(finalPayment.invoice().status()).isEqualTo(InvoiceStatus.PAID);
  }
}
