package com.github.nambuntu.netsuite.ch09;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderOperationalStateProjectorTest {

  private final OrderOperationalStateProjector projector = new OrderOperationalStateProjector();

  @Test
  void yieldsPendingFulfillmentWhenNoEventsExist() {
    ProjectedOrderOperationalView projection = projector.project(theoOrder(), List.of());

    assertThat(projection.operationalState()).isEqualTo(OrderOperationalState.PENDING_FULFILLMENT);
    assertThat(projection.invoiceContext().financialState()).isEqualTo("INVOICED");
  }

  @Test
  void packedEventDoesNotIncreaseFulfilledQuantity() {
    ProjectedOrderOperationalView projection = projector.project(theoOrder(), List.of(
        new ItemFulfillment(
            "if-theo-1001-0",
            "so-theo-1001",
            FulfillmentStage.PACKED,
            "warehouse-check",
            List.of(
                new ItemFulfillmentLine("line-nimbus-cat-body", 1),
                new ItemFulfillmentLine("line-accessory-bundle", 1),
                new ItemFulfillmentLine("line-personality-pack", 1)))));

    assertThat(projection.operationalState()).isEqualTo(OrderOperationalState.PENDING_FULFILLMENT);
    assertThat(projection.lineViews()).extracting(OrderOperationalLineView::fulfilledQuantity)
        .containsExactly(0, 0, 0);
  }

  @Test
  void firstShippedEventMakesOrderPartiallyFulfilled() {
    ProjectedOrderOperationalView projection = projector.project(theoOrder(), List.of(
        new ItemFulfillment(
            "if-theo-1001-0",
            "so-theo-1001",
            FulfillmentStage.PACKED,
            "warehouse-check",
            List.of(
                new ItemFulfillmentLine("line-nimbus-cat-body", 1),
                new ItemFulfillmentLine("line-accessory-bundle", 1),
                new ItemFulfillmentLine("line-personality-pack", 1))),
        new ItemFulfillment(
            "if-theo-1001-1",
            "so-theo-1001",
            FulfillmentStage.SHIPPED,
            "express",
            List.of(
                new ItemFulfillmentLine("line-nimbus-cat-body", 1),
                new ItemFulfillmentLine("line-personality-pack", 1)))));

    assertThat(projection.operationalState()).isEqualTo(OrderOperationalState.PARTIALLY_FULFILLED);
    assertThat(projection.lineViews()).extracting(OrderOperationalLineView::fulfilledQuantity)
        .containsExactly(1, 0, 1);
    assertThat(projection.invoiceContext().financialState()).isEqualTo("INVOICED");
  }

  @Test
  void secondShippedEventMakesOrderFulfilled() {
    ProjectedOrderOperationalView projection = projector.project(theoOrder(), List.of(
        new ItemFulfillment(
            "if-theo-1001-0",
            "so-theo-1001",
            FulfillmentStage.PACKED,
            "warehouse-check",
            List.of(
                new ItemFulfillmentLine("line-nimbus-cat-body", 1),
                new ItemFulfillmentLine("line-accessory-bundle", 1),
                new ItemFulfillmentLine("line-personality-pack", 1))),
        new ItemFulfillment(
            "if-theo-1001-1",
            "so-theo-1001",
            FulfillmentStage.SHIPPED,
            "express",
            List.of(
                new ItemFulfillmentLine("line-nimbus-cat-body", 1),
                new ItemFulfillmentLine("line-personality-pack", 1))),
        new ItemFulfillment(
            "if-theo-1001-2",
            "so-theo-1001",
            FulfillmentStage.SHIPPED,
            "standard",
            List.of(new ItemFulfillmentLine("line-accessory-bundle", 1)))));

    assertThat(projection.operationalState()).isEqualTo(OrderOperationalState.FULFILLED);
    assertThat(projection.lineViews()).extracting(OrderOperationalLineView::fulfilledQuantity)
        .containsExactly(1, 1, 1);
  }

  private static SalesOrder theoOrder() {
    return new SalesOrder(
        "so-theo-1001",
        "customer-theo-tran",
        new InvoiceContext("inv-theo-1001", "INVOICED"),
        List.of(
            new SalesOrderLine("line-nimbus-cat-body", "Nimbus Cat body", 1),
            new SalesOrderLine("line-accessory-bundle", "Accessory bundle", 1),
            new SalesOrderLine("line-personality-pack", "Personality pack", 1)));
  }
}
