package com.github.nambuntu.netsuite.ch16.service;

import com.github.nambuntu.netsuite.ch16.gateway.MockTransformGateway;
import com.github.nambuntu.netsuite.ch16.model.DerivedFulfillment;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.DerivedReturnAuthorization;
import com.github.nambuntu.netsuite.ch16.model.SourceFulfillment;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrder;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import java.util.Objects;

public final class DerivedRecordPreviewService {

  private final InMemoryTransformationStore store;
  private final MockTransformGateway transformGateway;

  public DerivedRecordPreviewService(InMemoryTransformationStore store, MockTransformGateway transformGateway) {
    this.store = Objects.requireNonNull(store, "store");
    this.transformGateway = Objects.requireNonNull(transformGateway, "transformGateway");
  }

  public DerivedInvoice previewInvoiceFromSalesOrder(String salesOrderExternalId) {
    return transformGateway.previewInvoiceFromSalesOrder(sourceSalesOrder(salesOrderExternalId));
  }

  public DerivedFulfillment previewFulfillmentFromSalesOrder(String salesOrderExternalId) {
    return transformGateway.previewFulfillmentFromSalesOrder(sourceSalesOrder(salesOrderExternalId));
  }

  public DerivedReturnAuthorization previewReturnAuthorizationFromSalesOrder(String salesOrderExternalId) {
    return transformGateway.previewReturnAuthorizationFromSalesOrder(sourceSalesOrder(salesOrderExternalId));
  }

  public DerivedReturnAuthorization previewReturnAuthorizationFromFulfillment(String fulfillmentExternalId) {
    SourceFulfillment fulfillment = store.requireFulfillment(fulfillmentExternalId);
    return transformGateway.previewReturnAuthorizationFromFulfillment(
        fulfillment,
        sourceSalesOrder(fulfillment.sourceSalesOrderExternalId()));
  }

  private SourceSalesOrder sourceSalesOrder(String salesOrderExternalId) {
    return store.requireSalesOrder(salesOrderExternalId);
  }
}
