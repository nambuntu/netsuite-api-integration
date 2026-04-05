package com.github.nambuntu.netsuite.ch09;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderFulfillmentWorkflowTest {

  @Test
  void scenarioIncludesThreeLinesThreeEventsAndReadableProgression() {
    OrderFulfillmentWorkflow.FulfillmentScenarioResult scenario = new OrderFulfillmentWorkflow().runTheoScenario();

    assertThat(scenario.initialState().operationalState()).isEqualTo(OrderOperationalState.PENDING_FULFILLMENT);
    assertThat(scenario.afterPacked().operationalState()).isEqualTo(OrderOperationalState.PENDING_FULFILLMENT);
    assertThat(scenario.afterFirstShipment().operationalState()).isEqualTo(OrderOperationalState.PARTIALLY_FULFILLED);
    assertThat(scenario.afterSecondShipment().operationalState()).isEqualTo(OrderOperationalState.FULFILLED);
    assertThat(scenario.afterSecondShipment().fulfillmentEvents())
        .extracting(ItemFulfillment::externalId)
        .containsExactly("if-theo-1001-0", "if-theo-1001-1", "if-theo-1001-2");
    assertThat(scenario.afterFirstShipment().lineViews())
        .extracting(OrderOperationalLineView::fulfilledQuantity)
        .containsExactly(1, 0, 1);
    assertThat(scenario.afterSecondShipment().lineViews())
        .extracting(OrderOperationalLineView::fulfilledQuantity)
        .containsExactly(1, 1, 1);
    assertThat(scenario.afterFirstShipment().invoiceContext().financialState()).isEqualTo("INVOICED");
  }

  @Test
  void renderDemoShowsInitialPackedPartialAndFinalSections() {
    assertThat(FulfillmentStateDemo.renderDemo()).isEqualTo("""
        Order: so-theo-1001
        Customer: customer-theo-tran
        Financial state: INVOICED (invoice inv-theo-1001)
        Operational state: PENDING_FULFILLMENT
        Lines:
        - Nimbus Cat body........ ordered 1, fulfilled 0
        - Accessory bundle....... ordered 1, fulfilled 0
        - Personality pack....... ordered 1, fulfilled 0
        Fulfillment events:
        - none
        
        After packed event
        Financial state: INVOICED (invoice inv-theo-1001)
        Operational state: PENDING_FULFILLMENT
        Lines:
        - Nimbus Cat body........ ordered 1, fulfilled 0
        - Accessory bundle....... ordered 1, fulfilled 0
        - Personality pack....... ordered 1, fulfilled 0
        Fulfillment events:
        - if-theo-1001-0  PACKED  warehouse-check
        
        After first shipment
        Financial state: INVOICED (invoice inv-theo-1001)
        Operational state: PARTIALLY_FULFILLED
        Lines:
        - Nimbus Cat body........ ordered 1, fulfilled 1
        - Accessory bundle....... ordered 1, fulfilled 0
        - Personality pack....... ordered 1, fulfilled 1
        Fulfillment events:
        - if-theo-1001-0  PACKED  warehouse-check
        - if-theo-1001-1  SHIPPED express
        
        After second shipment
        Financial state: INVOICED (invoice inv-theo-1001)
        Operational state: FULFILLED
        Lines:
        - Nimbus Cat body........ ordered 1, fulfilled 1
        - Accessory bundle....... ordered 1, fulfilled 1
        - Personality pack....... ordered 1, fulfilled 1
        Fulfillment events:
        - if-theo-1001-0  PACKED  warehouse-check
        - if-theo-1001-1  SHIPPED express
        - if-theo-1001-2  SHIPPED standard
        """);
  }
}
