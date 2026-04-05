package com.github.nambuntu.netsuite.ch09;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class FulfillmentService {

  private final InMemorySalesOrderStore salesOrderStore;
  private final InMemoryFulfillmentStore fulfillmentStore;

  public FulfillmentService(
      InMemorySalesOrderStore salesOrderStore,
      InMemoryFulfillmentStore fulfillmentStore) {
    this.salesOrderStore = Objects.requireNonNull(salesOrderStore, "salesOrderStore");
    this.fulfillmentStore = Objects.requireNonNull(fulfillmentStore, "fulfillmentStore");
  }

  public ItemFulfillment recordFulfillment(ItemFulfillment fulfillment) {
    SalesOrder order = salesOrderStore.require(fulfillment.salesOrderExternalId());
    Set<String> seenLineIds = new HashSet<>();
    Map<String, Integer> shippedQuantitiesByLine = fulfillmentStore.findBySalesOrderExternalId(order.externalId()).stream()
        .filter(event -> event.stage() == FulfillmentStage.SHIPPED)
        .flatMap(event -> event.lines().stream())
        .collect(Collectors.toMap(
            ItemFulfillmentLine::salesOrderLineExternalId,
            ItemFulfillmentLine::fulfilledQuantity,
            Integer::sum));

    for (ItemFulfillmentLine line : fulfillment.lines()) {
      if (!seenLineIds.add(line.salesOrderLineExternalId())) {
        throw new IllegalArgumentException("duplicate fulfillment line for sales order line: " + line.salesOrderLineExternalId());
      }
      SalesOrderLine orderLine = order.requireLine(line.salesOrderLineExternalId());
      if (line.fulfilledQuantity() > orderLine.orderedQuantity()) {
        throw new IllegalArgumentException("fulfillment quantity exceeds ordered quantity for line: " + line.salesOrderLineExternalId());
      }
      if (fulfillment.stage() == FulfillmentStage.SHIPPED) {
        int existingShipped = shippedQuantitiesByLine.getOrDefault(line.salesOrderLineExternalId(), 0);
        if (existingShipped + line.fulfilledQuantity() > orderLine.orderedQuantity()) {
          throw new IllegalArgumentException("shipped quantity would exceed ordered quantity for line: " + line.salesOrderLineExternalId());
        }
      }
    }

    fulfillmentStore.save(fulfillment);
    return fulfillment;
  }
}
