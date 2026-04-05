package com.github.nambuntu.netsuite.ch07;

import java.util.List;

public final class SalesOrderDemo {

  private SalesOrderDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    CustomerFixtureRepository customerFixture = new CustomerFixtureRepository();
    ItemFixtureRepository itemFixture = new ItemFixtureRepository();
    InMemorySalesOrderRepository salesOrderRepository = new InMemorySalesOrderRepository();
    SalesOrderService salesOrderService =
        new SalesOrderService(customerFixture, itemFixture, salesOrderRepository);
    SalesOrderSummaryFormatter orderSummaryFormatter = new SalesOrderSummaryFormatter();

    OrderCommand command = new OrderCommand(
        "so-theo-1001",
        "customer-theo-tran",
        List.of(
            new OrderLineCommand("item-nimbus-cat-midnight-blue", 1),
            new OrderLineCommand("item-personality-pack-quiet-evenings", 1),
            new OrderLineCommand("item-accessory-bundle-apartment", 1)
        )
    );

    SalesOrder created = salesOrderService.createOrder(command);
    return orderSummaryFormatter.format(created);
  }
}
