package com.github.nambuntu.netsuite.ch16.gateway;

import com.github.nambuntu.netsuite.ch16.model.DerivedFulfillment;
import com.github.nambuntu.netsuite.ch16.model.DerivedFulfillmentLine;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoiceLine;
import com.github.nambuntu.netsuite.ch16.model.DerivedReturnAuthorization;
import com.github.nambuntu.netsuite.ch16.model.DerivedReturnLine;
import com.github.nambuntu.netsuite.ch16.model.SourceFulfillment;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrder;
import com.github.nambuntu.netsuite.ch16.model.TransformSourceKind;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MockTransformGateway {

  private final InMemoryTransformationStore store;

  public MockTransformGateway(InMemoryTransformationStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public DerivedInvoice previewInvoiceFromSalesOrder(SourceSalesOrder salesOrder) {
    return new DerivedInvoice(
        invoiceExternalId(salesOrder.externalId()),
        salesOrder.externalId(),
        salesOrder.customerExternalId(),
        salesOrder.lines().stream()
            .map(line -> new DerivedInvoiceLine(line.externalId(), line.itemExternalId(), line.quantity(), line.amount()))
            .toList(),
        null,
        null,
        null);
  }

  public void saveInvoice(DerivedInvoice invoice) {
    store.saveInvoice(invoice);
  }

  public DerivedFulfillment previewFulfillmentFromSalesOrder(SourceSalesOrder salesOrder) {
    return new DerivedFulfillment(
        fulfillmentExternalId(salesOrder.externalId()),
        salesOrder.externalId(),
        salesOrder.customerExternalId(),
        salesOrder.lines().stream()
            .map(line -> new DerivedFulfillmentLine(line.externalId(), line.itemExternalId(), line.quantity()))
            .toList(),
        null,
        null,
        null);
  }

  public void saveFulfillment(DerivedFulfillment fulfillment) {
    store.saveDerivedFulfillment(fulfillment);
  }

  public DerivedReturnAuthorization previewReturnAuthorizationFromSalesOrder(SourceSalesOrder salesOrder) {
    return new DerivedReturnAuthorization(
        returnAuthorizationExternalIdFromSalesOrder(salesOrder.externalId()),
        TransformSourceKind.SALES_ORDER,
        salesOrder.externalId(),
        salesOrder.externalId(),
        salesOrder.customerExternalId(),
        salesOrder.lines().stream()
            .map(line -> new DerivedReturnLine(line.externalId(), line.itemExternalId(), line.quantity()))
            .toList(),
        null,
        null,
        null);
  }

  public DerivedReturnAuthorization previewReturnAuthorizationFromFulfillment(
      SourceFulfillment fulfillment,
      SourceSalesOrder salesOrder) {
    Map<String, String> itemByLine = new LinkedHashMap<>();
    salesOrder.lines().forEach(line -> itemByLine.put(line.externalId(), line.itemExternalId()));
    return new DerivedReturnAuthorization(
        returnAuthorizationExternalIdFromFulfillment(fulfillment.externalId()),
        TransformSourceKind.FULFILLMENT,
        fulfillment.externalId(),
        fulfillment.sourceSalesOrderExternalId(),
        fulfillment.customerExternalId(),
        fulfillment.fulfilledSalesOrderLineReferences().stream()
            .map(lineReference -> new DerivedReturnLine(lineReference, itemByLine.get(lineReference), 1))
            .toList(),
        null,
        null,
        null);
  }

  public void saveReturnAuthorization(DerivedReturnAuthorization returnAuthorization) {
    store.saveReturnAuthorization(returnAuthorization);
  }

  private static String invoiceExternalId(String salesOrderExternalId) {
    return salesOrderExternalId.replaceFirst("^so-", "inv-");
  }

  private static String fulfillmentExternalId(String salesOrderExternalId) {
    return salesOrderExternalId.replaceFirst("^so-", "if-");
  }

  private static String returnAuthorizationExternalIdFromSalesOrder(String salesOrderExternalId) {
    return salesOrderExternalId.replaceFirst("^so-", "ra-");
  }

  private static String returnAuthorizationExternalIdFromFulfillment(String fulfillmentExternalId) {
    return fulfillmentExternalId.replaceFirst("^if-", "ra-") + "-from-fulfillment";
  }
}
