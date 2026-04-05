package com.github.nambuntu.netsuite.ch07;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class InMemorySalesOrderRepository {

  private final Map<String, SalesOrder> ordersByExternalId = new LinkedHashMap<>();

  public SalesOrder create(
      String externalId,
      Customer customer,
      List<SalesOrderLine> lines,
      OrderStatus status) {

    Objects.requireNonNull(customer, "customer");
    Objects.requireNonNull(lines, "lines");
    Objects.requireNonNull(status, "status");
    if (ordersByExternalId.containsKey(externalId)) {
      throw new IllegalStateException("sales order already exists for external ID: " + externalId);
    }
    SalesOrder salesOrder = new SalesOrder(externalId, customer, lines, status);
    ordersByExternalId.put(externalId, salesOrder);
    return salesOrder;
  }

  public SalesOrder require(String externalId) {
    SalesOrder salesOrder = ordersByExternalId.get(externalId);
    if (salesOrder == null) {
      throw new IllegalArgumentException("unknown sales order external ID: " + externalId);
    }
    return salesOrder;
  }

  public int countOrders() {
    return ordersByExternalId.size();
  }
}
