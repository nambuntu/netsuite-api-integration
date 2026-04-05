package com.github.nambuntu.netsuite.ch16.store;

import com.github.nambuntu.netsuite.ch16.model.DerivedFulfillment;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.DerivedReturnAuthorization;
import com.github.nambuntu.netsuite.ch16.model.SourceFulfillment;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrder;
import java.util.LinkedHashMap;
import java.util.Map;

public final class InMemoryTransformationStore {

  private final Map<String, SourceSalesOrder> salesOrders = new LinkedHashMap<>();
  private final Map<String, SourceFulfillment> fulfillments = new LinkedHashMap<>();
  private final Map<String, DerivedInvoice> invoices = new LinkedHashMap<>();
  private final Map<String, DerivedFulfillment> derivedFulfillments = new LinkedHashMap<>();
  private final Map<String, DerivedReturnAuthorization> returnAuthorizations = new LinkedHashMap<>();

  public void saveSourceSalesOrder(SourceSalesOrder salesOrder) {
    salesOrders.put(salesOrder.externalId(), salesOrder);
  }

  public SourceSalesOrder requireSalesOrder(String salesOrderExternalId) {
    SourceSalesOrder salesOrder = salesOrders.get(salesOrderExternalId);
    if (salesOrder == null) {
      throw new IllegalArgumentException("Unknown sales order: " + salesOrderExternalId);
    }
    return salesOrder;
  }

  public void saveSourceFulfillment(SourceFulfillment fulfillment) {
    fulfillments.put(fulfillment.externalId(), fulfillment);
  }

  public SourceFulfillment requireFulfillment(String fulfillmentExternalId) {
    SourceFulfillment fulfillment = fulfillments.get(fulfillmentExternalId);
    if (fulfillment == null) {
      throw new IllegalArgumentException("Unknown fulfillment: " + fulfillmentExternalId);
    }
    return fulfillment;
  }

  public void saveInvoice(DerivedInvoice invoice) {
    invoices.put(invoice.externalId(), invoice);
  }

  public void saveDerivedFulfillment(DerivedFulfillment fulfillment) {
    derivedFulfillments.put(fulfillment.externalId(), fulfillment);
  }

  public void saveReturnAuthorization(DerivedReturnAuthorization returnAuthorization) {
    returnAuthorizations.put(returnAuthorization.externalId(), returnAuthorization);
  }
}
