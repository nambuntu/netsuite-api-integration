package com.github.nambuntu.netsuite.ch09;

import java.util.List;
import java.util.Objects;

public final class OrderFulfillmentWorkflow {

  private final InMemorySalesOrderStore salesOrderStore;
  private final InMemoryFulfillmentStore fulfillmentStore;
  private final FulfillmentService fulfillmentService;
  private final OrderOperationalStateProjector projector;

  public OrderFulfillmentWorkflow() {
    this(new InMemorySalesOrderStore(), new InMemoryFulfillmentStore());
  }

  public OrderFulfillmentWorkflow(
      InMemorySalesOrderStore salesOrderStore,
      InMemoryFulfillmentStore fulfillmentStore) {
    this.salesOrderStore = Objects.requireNonNull(salesOrderStore, "salesOrderStore");
    this.fulfillmentStore = Objects.requireNonNull(fulfillmentStore, "fulfillmentStore");
    this.fulfillmentService = new FulfillmentService(salesOrderStore, fulfillmentStore);
    this.projector = new OrderOperationalStateProjector();
  }

  public FulfillmentScenarioResult runTheoScenario() {
    SalesOrder order = buildTheoOrder();
    salesOrderStore.save(order);

    ProjectedOrderOperationalView initialState = projector.project(order, List.of());

    fulfillmentService.recordFulfillment(new ItemFulfillment(
        "if-theo-1001-0",
        order.externalId(),
        FulfillmentStage.PACKED,
        "warehouse-check",
        List.of(
            new ItemFulfillmentLine("line-nimbus-cat-body", 1),
            new ItemFulfillmentLine("line-accessory-bundle", 1),
            new ItemFulfillmentLine("line-personality-pack", 1))));
    ProjectedOrderOperationalView afterPacked = currentProjection(order.externalId());

    fulfillmentService.recordFulfillment(new ItemFulfillment(
        "if-theo-1001-1",
        order.externalId(),
        FulfillmentStage.SHIPPED,
        "express",
        List.of(
            new ItemFulfillmentLine("line-nimbus-cat-body", 1),
            new ItemFulfillmentLine("line-personality-pack", 1))));
    ProjectedOrderOperationalView afterFirstShipment = currentProjection(order.externalId());

    fulfillmentService.recordFulfillment(new ItemFulfillment(
        "if-theo-1001-2",
        order.externalId(),
        FulfillmentStage.SHIPPED,
        "standard",
        List.of(new ItemFulfillmentLine("line-accessory-bundle", 1))));
    ProjectedOrderOperationalView afterSecondShipment = currentProjection(order.externalId());

    return new FulfillmentScenarioResult(initialState, afterPacked, afterFirstShipment, afterSecondShipment);
  }

  private ProjectedOrderOperationalView currentProjection(String salesOrderExternalId) {
    SalesOrder order = salesOrderStore.require(salesOrderExternalId);
    List<ItemFulfillment> fulfillments = fulfillmentStore.findBySalesOrderExternalId(salesOrderExternalId);
    return projector.project(order, fulfillments);
  }

  private static SalesOrder buildTheoOrder() {
    return new SalesOrder(
        "so-theo-1001",
        "customer-theo-tran",
        new InvoiceContext("inv-theo-1001", "INVOICED"),
        List.of(
            new SalesOrderLine("line-nimbus-cat-body", "Nimbus Cat body", 1),
            new SalesOrderLine("line-accessory-bundle", "Accessory bundle", 1),
            new SalesOrderLine("line-personality-pack", "Personality pack", 1)));
  }

  public record FulfillmentScenarioResult(
      ProjectedOrderOperationalView initialState,
      ProjectedOrderOperationalView afterPacked,
      ProjectedOrderOperationalView afterFirstShipment,
      ProjectedOrderOperationalView afterSecondShipment) {

    public FulfillmentScenarioResult {
      Objects.requireNonNull(initialState, "initialState");
      Objects.requireNonNull(afterPacked, "afterPacked");
      Objects.requireNonNull(afterFirstShipment, "afterFirstShipment");
      Objects.requireNonNull(afterSecondShipment, "afterSecondShipment");
    }
  }
}
