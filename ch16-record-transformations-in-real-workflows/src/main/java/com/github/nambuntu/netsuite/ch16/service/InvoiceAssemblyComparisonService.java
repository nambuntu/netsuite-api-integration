package com.github.nambuntu.netsuite.ch16.service;

import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoiceLine;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrder;
import com.github.nambuntu.netsuite.ch16.model.TransformPatch;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import java.util.Objects;

public final class InvoiceAssemblyComparisonService {

  private final InMemoryTransformationStore store;

  public InvoiceAssemblyComparisonService(InMemoryTransformationStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public DerivedInvoice assembleInvoiceManually(String salesOrderExternalId, TransformPatch patch) {
    SourceSalesOrder salesOrder = store.requireSalesOrder(salesOrderExternalId);
    return new DerivedInvoice(
        "manual-" + salesOrderExternalId.replaceFirst("^so-", "inv-"),
        null,
        salesOrder.customerExternalId(),
        salesOrder.lines().stream()
            .map(line -> new DerivedInvoiceLine(null, line.itemExternalId(), line.quantity(), line.amount()))
            .toList(),
        patch.memo(),
        patch.location(),
        null);
  }
}
