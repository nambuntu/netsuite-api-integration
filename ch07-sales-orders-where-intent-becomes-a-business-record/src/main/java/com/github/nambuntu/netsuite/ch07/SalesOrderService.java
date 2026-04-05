package com.github.nambuntu.netsuite.ch07;

import java.util.List;
import java.util.Objects;

public final class SalesOrderService {

  private final CustomerFixtureRepository customerFixture;
  private final ItemFixtureRepository itemFixture;
  private final InMemorySalesOrderRepository salesOrderRepository;

  public SalesOrderService(
      CustomerFixtureRepository customerFixture,
      ItemFixtureRepository itemFixture,
      InMemorySalesOrderRepository salesOrderRepository) {
    this.customerFixture = Objects.requireNonNull(customerFixture, "customerFixture");
    this.itemFixture = Objects.requireNonNull(itemFixture, "itemFixture");
    this.salesOrderRepository = Objects.requireNonNull(salesOrderRepository, "salesOrderRepository");
  }

  public SalesOrder createOrder(OrderCommand command) {
    Customer customer = customerFixture.require(command.customerExternalId());
    List<SalesOrderLine> lines = itemFixture.resolve(command.lines());

    salesOrderRepository.create(
        command.orderExternalId(),
        customer,
        lines,
        OrderStatus.PENDING_FULFILLMENT);

    return salesOrderRepository.require(command.orderExternalId());
  }

  public SalesOrder requireOrder(String orderExternalId) {
    return salesOrderRepository.require(orderExternalId);
  }
}
