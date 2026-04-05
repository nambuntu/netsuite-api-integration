package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class OrderToCashStore {

  private final Map<String, Invoice> invoicesByExternalId = new LinkedHashMap<>();
  private final Map<String, CustomerPayment> paymentsByExternalId = new LinkedHashMap<>();

  public void saveInvoice(Invoice invoice) {
    if (invoicesByExternalId.containsKey(invoice.externalId())) {
      throw new IllegalStateException("invoice already exists for external ID: " + invoice.externalId());
    }
    invoicesByExternalId.put(invoice.externalId(), invoice);
  }

  public Invoice requireInvoice(String invoiceExternalId) {
    Invoice invoice = invoicesByExternalId.get(invoiceExternalId);
    if (invoice == null) {
      throw new IllegalArgumentException("unknown invoice external ID: " + invoiceExternalId);
    }
    return invoice;
  }

  public void assertPaymentExternalIdAvailable(String paymentExternalId) {
    if (paymentsByExternalId.containsKey(paymentExternalId)) {
      throw new IllegalStateException("payment already exists for external ID: " + paymentExternalId);
    }
  }

  public void savePayment(CustomerPayment payment) {
    paymentsByExternalId.put(payment.externalId(), payment);
  }

  public Invoice applyPayment(String invoiceExternalId, BigDecimal amount) {
    Invoice invoice = requireInvoice(invoiceExternalId);
    Invoice updatedInvoice = invoice.applyPayment(amount);
    invoicesByExternalId.put(invoiceExternalId, updatedInvoice);
    return updatedInvoice;
  }

  public List<OpenInvoiceBalance> findOpenInvoicesForCustomer(String customerExternalId) {
    List<OpenInvoiceBalance> openInvoices = new ArrayList<>();
    for (Invoice invoice : invoicesByExternalId.values()) {
      if (invoice.customerExternalId().equals(customerExternalId) && invoice.amountDue().signum() > 0) {
        openInvoices.add(new OpenInvoiceBalance(
            invoice.externalId(),
            invoice.customerExternalId(),
            invoice.createdFromSalesOrderExternalId(),
            invoice.totalAmount(),
            invoice.amountDue(),
            invoice.currencyCode(),
            invoice.status()));
      }
    }
    return List.copyOf(openInvoices);
  }
}
