package com.github.nambuntu.netsuite.ch16.fixture;

import com.github.nambuntu.netsuite.ch16.model.SourceFulfillment;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrder;
import com.github.nambuntu.netsuite.ch16.model.SourceSalesOrderLine;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import java.math.BigDecimal;
import java.util.List;

public final class TransformationFixtures {

  private TransformationFixtures() {
  }

  public static InMemoryTransformationStore seededStore() {
    InMemoryTransformationStore store = new InMemoryTransformationStore();
    store.saveSourceSalesOrder(new SourceSalesOrder(
        "so-theo-1001",
        "customer-theo-tran",
        List.of(
            new SourceSalesOrderLine("so-theo-1001-line-1", "item-nimbus-cat-midnight-blue", 1, new BigDecimal("199.00")),
            new SourceSalesOrderLine("so-theo-1001-line-2", "item-personality-pack-quiet", 1, new BigDecimal("50.00")))));
    store.saveSourceFulfillment(new SourceFulfillment(
        "if-theo-1001",
        "so-theo-1001",
        "customer-theo-tran",
        List.of("so-theo-1001-line-1", "so-theo-1001-line-2")));
    return store;
  }
}
