package com.github.nambuntu.netsuite.ch07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class SalesOrderServiceTest {

  @Test
  void createOrderBuildsOnePendingFulfillmentSalesOrderWithThreeLines() {
    InMemorySalesOrderRepository repository = new InMemorySalesOrderRepository();
    SalesOrderService service = new SalesOrderService(
        new CustomerFixtureRepository(),
        new ItemFixtureRepository(),
        repository);

    SalesOrder created = service.createOrder(new OrderCommand(
        "so-theo-1001",
        "customer-theo-tran",
        List.of(
            new OrderLineCommand("item-nimbus-cat-midnight-blue", 1),
            new OrderLineCommand("item-personality-pack-quiet-evenings", 1),
            new OrderLineCommand("item-accessory-bundle-apartment", 1)
        )));

    assertThat(created.externalId()).isEqualTo("so-theo-1001");
    assertThat(created.customer().externalId()).isEqualTo("customer-theo-tran");
    assertThat(created.status()).isEqualTo(OrderStatus.PENDING_FULFILLMENT);
    assertThat(created.lines()).extracting(SalesOrderLine::itemExternalId).containsExactly(
        "item-nimbus-cat-midnight-blue",
        "item-personality-pack-quiet-evenings",
        "item-accessory-bundle-apartment");
    assertThat(repository.countOrders()).isEqualTo(1);
    assertThat(service.requireOrder("so-theo-1001")).isEqualTo(created);
  }

  @Test
  void createOrderRejectsUnknownCustomerClearly() {
    SalesOrderService service = new SalesOrderService(
        new CustomerFixtureRepository(),
        new ItemFixtureRepository(),
        new InMemorySalesOrderRepository());

    assertThatThrownBy(() -> service.createOrder(new OrderCommand(
        "so-theo-1001",
        "customer-unknown",
        List.of(new OrderLineCommand("item-nimbus-cat-midnight-blue", 1)))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("unknown customer external ID");
  }

  @Test
  void createOrderRejectsUnknownItemClearly() {
    SalesOrderService service = new SalesOrderService(
        new CustomerFixtureRepository(),
        new ItemFixtureRepository(),
        new InMemorySalesOrderRepository());

    assertThatThrownBy(() -> service.createOrder(new OrderCommand(
        "so-theo-1001",
        "customer-theo-tran",
        List.of(new OrderLineCommand("item-unknown", 1)))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("unknown item external ID");
  }
}
