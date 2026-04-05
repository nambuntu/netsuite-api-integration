package com.github.nambuntu.netsuite.ch10.service;

import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlClient;
import com.github.nambuntu.netsuite.ch10.model.OrderReconciliationRow;
import com.github.nambuntu.netsuite.ch10.model.ReconciliationState;
import com.github.nambuntu.netsuite.ch10.query.FinanceOpsQueries;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ReconciliationReportService {

  private final SuiteQlClient suiteQlClient;

  public ReconciliationReportService(SuiteQlClient suiteQlClient) {
    this.suiteQlClient = Objects.requireNonNull(suiteQlClient, "suiteQlClient");
  }

  public List<OrderReconciliationRow> fullReport() {
    return suiteQlClient.query(FinanceOpsQueries.ORDER_RECONCILIATION_VIEW, Map.of()).stream()
        .map(this::classify)
        .toList();
  }

  public List<OrderReconciliationRow> attentionReport() {
    return suiteQlClient.query(FinanceOpsQueries.OPEN_OR_MISMATCHED_ORDERS, Map.of()).stream()
        .map(this::classify)
        .toList();
  }

  OrderReconciliationRow classify(OrderReconciliationRow row) {
    BigDecimal due = row.amountDue();
    boolean shipped = "SHIPPED".equals(row.fulfillmentState());
    ReconciliationState state = due.compareTo(BigDecimal.ZERO) > 0 && shipped
        ? ReconciliationState.OPEN_BALANCE
        : due.compareTo(BigDecimal.ZERO) == 0 && shipped
            ? ReconciliationState.SETTLED
            : due.compareTo(BigDecimal.ZERO) == 0
                ? ReconciliationState.FULFILLMENT_LAG
                : ReconciliationState.STATE_MISMATCH;
    return row.withReconciliationState(state);
  }
}
