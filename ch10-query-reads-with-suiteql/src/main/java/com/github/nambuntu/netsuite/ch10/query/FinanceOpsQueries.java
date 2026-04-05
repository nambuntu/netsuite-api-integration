package com.github.nambuntu.netsuite.ch10.query;

import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch10.model.OrderReconciliationRow;
import com.github.nambuntu.netsuite.ch10.model.ReconciliationState;

public final class FinanceOpsQueries {

  public static final SuiteQlQuery<OrderReconciliationRow> ORDER_RECONCILIATION_VIEW = new SuiteQlQuery<>(
      "financeOps.orderReconciliationView",
      """
      SELECT
        customer.externalid AS customer_externalid,
        salesorder.externalid AS salesorder_externalid,
        invoice.externalid AS invoice_externalid,
        invoice.total AS invoice_total,
        payment.amountapplied AS amount_paid,
        invoice.amountremaining AS amount_due,
        payment.status AS payment_state,
        fulfillment.status AS fulfillment_state,
        reconciliation.state AS reconciliation_state
      FROM custom_reconciliation_view
      ORDER BY salesorder.externalid
      """,
      FinanceOpsQueries::mapOrderReconciliationRow);

  public static final SuiteQlQuery<OrderReconciliationRow> OPEN_OR_MISMATCHED_ORDERS = new SuiteQlQuery<>(
      "financeOps.openOrMismatchedOrders",
      """
      SELECT
        customer.externalid AS customer_externalid,
        salesorder.externalid AS salesorder_externalid,
        invoice.externalid AS invoice_externalid,
        invoice.total AS invoice_total,
        payment.amountapplied AS amount_paid,
        invoice.amountremaining AS amount_due,
        payment.status AS payment_state,
        fulfillment.status AS fulfillment_state,
        reconciliation.state AS reconciliation_state
      FROM custom_reconciliation_view
      WHERE reconciliation.state <> 'SETTLED'
      ORDER BY salesorder.externalid
      """,
      FinanceOpsQueries::mapOrderReconciliationRow);

  private FinanceOpsQueries() {
  }

  public static OrderReconciliationRow mapOrderReconciliationRow(SuiteQlRow row) {
    return new OrderReconciliationRow(
        row.string("customer_externalid"),
        row.string("salesorder_externalid"),
        row.string("invoice_externalid"),
        row.decimal("invoice_total"),
        row.decimal("amount_paid"),
        row.decimal("amount_due"),
        row.string("payment_state"),
        row.string("fulfillment_state"),
        ReconciliationState.valueOf(row.string("reconciliation_state")));
  }
}
