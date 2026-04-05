package com.github.nambuntu.netsuite.ch09;

import java.util.LinkedHashMap;
import java.util.Map;

public final class InMemorySalesOrderStore {

  private final Map<String, SalesOrder> ordersByExternalId = new LinkedHashMap<>();

  public void save(SalesOrder order) {
    ordersByExternalId.put(order.externalId(), order);
  }

  public SalesOrder require(String salesOrderExternalId) {
    SalesOrder order = ordersByExternalId.get(salesOrderExternalId);
    if (order == null) {
      throw new IllegalArgumentException("unknown sales order external ID: " + salesOrderExternalId);
    }
    return order;
  }
}
