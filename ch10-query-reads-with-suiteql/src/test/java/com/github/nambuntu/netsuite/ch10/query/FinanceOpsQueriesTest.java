package com.github.nambuntu.netsuite.ch10.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlRow;
import com.github.nambuntu.netsuite.ch10.model.OrderReconciliationRow;
import com.github.nambuntu.netsuite.ch10.model.ReconciliationState;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FinanceOpsQueriesTest {

  @Test
  void mapsRowAndNormalizesAliasKeyCasing() {
    Map<String, Object> rawRow = new LinkedHashMap<>();
    rawRow.put("Customer_ExternalId", "customer-theo-tran");
    rawRow.put("SalesOrder_ExternalId", "so-theo-1001");
    rawRow.put("Invoice_ExternalId", "inv-theo-1001");
    rawRow.put("Invoice_Total", new BigDecimal("699.00"));
    rawRow.put("Amount_Paid", new BigDecimal("300.00"));
    rawRow.put("Amount_Due", new BigDecimal("399.00"));
    rawRow.put("Payment_State", "PARTIAL");
    rawRow.put("Fulfillment_State", "SHIPPED");
    rawRow.put("Reconciliation_State", "OPEN_BALANCE");

    OrderReconciliationRow row = FinanceOpsQueries.mapOrderReconciliationRow(new SuiteQlRow(rawRow));

    assertThat(row.customerExternalId()).isEqualTo("customer-theo-tran");
    assertThat(row.salesOrderExternalId()).isEqualTo("so-theo-1001");
    assertThat(row.invoiceExternalId()).isEqualTo("inv-theo-1001");
    assertThat(row.invoiceTotal()).isEqualByComparingTo("699.00");
    assertThat(row.amountPaid()).isEqualByComparingTo("300.00");
    assertThat(row.amountDue()).isEqualByComparingTo("399.00");
    assertThat(row.reconciliationState()).isEqualTo(ReconciliationState.OPEN_BALANCE);
  }
}
