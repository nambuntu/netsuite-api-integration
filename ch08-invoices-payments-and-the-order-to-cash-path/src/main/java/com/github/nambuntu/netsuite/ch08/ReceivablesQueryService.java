package com.github.nambuntu.netsuite.ch08;

import java.util.List;
import java.util.Objects;

public final class ReceivablesQueryService {

  private final OrderToCashStore store;

  public ReceivablesQueryService(OrderToCashStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public List<OpenInvoiceBalance> findOpenInvoicesForCustomer(String customerExternalId) {
    return store.findOpenInvoicesForCustomer(customerExternalId);
  }
}
