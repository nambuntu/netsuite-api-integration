package com.github.nambuntu.netsuite.ch09;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryFulfillmentStore {

  private final Map<String, ItemFulfillment> fulfillmentsByExternalId = new LinkedHashMap<>();
  private final Map<String, List<ItemFulfillment>> fulfillmentsBySalesOrderExternalId = new LinkedHashMap<>();

  public void save(ItemFulfillment fulfillment) {
    if (fulfillmentsByExternalId.containsKey(fulfillment.externalId())) {
      throw new IllegalStateException("fulfillment already exists for external ID: " + fulfillment.externalId());
    }
    fulfillmentsByExternalId.put(fulfillment.externalId(), fulfillment);
    fulfillmentsBySalesOrderExternalId
        .computeIfAbsent(fulfillment.salesOrderExternalId(), ignored -> new ArrayList<>())
        .add(fulfillment);
  }

  public List<ItemFulfillment> findBySalesOrderExternalId(String salesOrderExternalId) {
    return List.copyOf(fulfillmentsBySalesOrderExternalId.getOrDefault(salesOrderExternalId, List.of()));
  }
}
