package com.github.nambuntu.netsuite.ch10.mock;

import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlPage;
import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch10.query.SuiteQlQuery;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MockSuiteQlClient implements SuiteQlClient {

  private final MockNetSuiteStore store;

  public MockSuiteQlClient(MockNetSuiteStore store) {
    this.store = store;
  }

  @Override
  public <T> SuiteQlPage<T> queryPage(SuiteQlQuery<T> query, Map<String, Object> params, int offset, int limit) {
    List<Map<String, Object>> rawRows = switch (query.id()) {
      case "financeOps.orderReconciliationView" -> store.reconciliationScenarios().stream()
          .map(this::rowFromScenario)
          .toList();
      case "financeOps.openOrMismatchedOrders" -> store.attentionScenarios().stream()
          .map(this::rowFromScenario)
          .toList();
      default -> throw new IllegalArgumentException("Unsupported mock SuiteQL query: " + query.id());
    };

    List<T> mapped = new ArrayList<>();
    for (Map<String, Object> rawRow : rawRows) {
      mapped.add(query.rowMapper().apply(new SuiteQlRow(rawRow)));
    }

    int safeOffset = Math.max(0, offset);
    int safeLimit = Math.max(1, limit);
    int end = Math.min(mapped.size(), safeOffset + safeLimit);
    List<T> pagedItems = safeOffset >= mapped.size() ? List.of() : mapped.subList(safeOffset, end);
    return new SuiteQlPage<>(pagedItems, safeOffset, safeLimit, end < mapped.size(), mapped.size());
  }

  private Map<String, Object> rowFromScenario(MockNetSuiteStore.ReconciliationScenario scenario) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("customer_externalid", scenario.customerExternalId());
    row.put("salesorder_externalid", scenario.salesOrderExternalId());
    row.put("invoice_externalid", scenario.invoiceExternalId());
    row.put("invoice_total", scenario.invoiceTotal());
    row.put("amount_paid", scenario.amountPaid());
    row.put("amount_due", scenario.amountDue());
    row.put("payment_state", scenario.paymentState());
    row.put("fulfillment_state", scenario.fulfillmentState());
    row.put("reconciliation_state", scenario.reconciliationState().name());
    return row;
  }
}
