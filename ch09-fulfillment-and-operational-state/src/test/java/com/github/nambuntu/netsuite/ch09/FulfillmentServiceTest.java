package com.github.nambuntu.netsuite.ch09;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FulfillmentServiceTest {

  @Test
  void recordsSeparateFulfillmentEventsWithStableIdsAndPreservedHistory() {
    InMemorySalesOrderStore salesOrderStore = new InMemorySalesOrderStore();
    InMemoryFulfillmentStore fulfillmentStore = new InMemoryFulfillmentStore();
    FulfillmentService service = new FulfillmentService(salesOrderStore, fulfillmentStore);
    salesOrderStore.save(theoOrder());

    service.recordFulfillment(new ItemFulfillment(
        "if-theo-1001-0",
        "so-theo-1001",
        FulfillmentStage.PACKED,
        "warehouse-check",
        List.of(new ItemFulfillmentLine("line-nimbus-cat-body", 1))));
    service.recordFulfillment(new ItemFulfillment(
        "if-theo-1001-1",
        "so-theo-1001",
        FulfillmentStage.SHIPPED,
        "express",
        List.of(new ItemFulfillmentLine("line-nimbus-cat-body", 1))));

    assertThat(fulfillmentStore.findBySalesOrderExternalId("so-theo-1001"))
        .extracting(ItemFulfillment::externalId)
        .containsExactly("if-theo-1001-0", "if-theo-1001-1");
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
