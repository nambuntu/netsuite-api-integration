package com.github.nambuntu.netsuite.ch09;

import java.util.List;
import java.util.Objects;

public record ProjectedOrderOperationalView(
    String salesOrderExternalId,
    String customerExternalId,
    InvoiceContext invoiceContext,
    OrderOperationalState operationalState,
    List<OrderOperationalLineView> lineViews,
    List<ItemFulfillment> fulfillmentEvents) {

  public ProjectedOrderOperationalView {
    salesOrderExternalId = requireText(salesOrderExternalId, "salesOrderExternalId");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    invoiceContext = Objects.requireNonNull(invoiceContext, "invoiceContext");
    operationalState = Objects.requireNonNull(operationalState, "operationalState");
    lineViews = List.copyOf(lineViews);
    fulfillmentEvents = List.copyOf(fulfillmentEvents);
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
