package com.github.nambuntu.netsuite.ch09;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class OrderOperationalStateProjector {

  public ProjectedOrderOperationalView project(SalesOrder order, List<ItemFulfillment> fulfillments) {
    Map<String, Integer> shippedQuantitiesByLine = new LinkedHashMap<>();
    for (ItemFulfillment fulfillment : fulfillments) {
      if (fulfillment.stage() != FulfillmentStage.SHIPPED) {
        continue;
      }
      for (ItemFulfillmentLine line : fulfillment.lines()) {
        shippedQuantitiesByLine.merge(line.salesOrderLineExternalId(), line.fulfilledQuantity(), Integer::sum);
      }
    }

    List<OrderOperationalLineView> lineViews = order.lines().stream()
        .map(line -> new OrderOperationalLineView(
            line.externalId(),
            line.itemName(),
            line.orderedQuantity(),
            shippedQuantitiesByLine.getOrDefault(line.externalId(), 0)))
        .toList();

    boolean allFulfilled = lineViews.stream()
        .allMatch(line -> line.fulfilledQuantity() >= line.orderedQuantity());
    boolean anyFulfilled = lineViews.stream()
        .anyMatch(line -> line.fulfilledQuantity() > 0);
    OrderOperationalState operationalState = allFulfilled
        ? OrderOperationalState.FULFILLED
        : anyFulfilled ? OrderOperationalState.PARTIALLY_FULFILLED : OrderOperationalState.PENDING_FULFILLMENT;

    return new ProjectedOrderOperationalView(
        order.externalId(),
        order.customerExternalId(),
        order.invoiceContext(),
        operationalState,
        lineViews,
        fulfillments);
  }
}
