package com.github.nambuntu.netsuite.ch19.facade;

import com.github.nambuntu.netsuite.ch19.model.LifecycleEvent;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.model.LifecycleStage;
import java.util.List;

public final class OrderLifecycleFacade {

  public List<LifecycleEvent> run(LifecycleScenario scenario) {
    return List.of(
        new LifecycleEvent(
            LifecycleStage.ORDER_AND_BILLING,
            scenario.customerExternalId()
                + " -> "
                + scenario.salesOrderExternalId()
                + " -> "
                + scenario.invoiceExternalId()
                + " -> "
                + scenario.paymentExternalId(),
            List.of(
                scenario.customerExternalId(),
                scenario.salesOrderExternalId(),
                scenario.invoiceExternalId(),
                scenario.paymentExternalId())),
        new LifecycleEvent(
            LifecycleStage.FULFILLMENT,
            scenario.salesOrderExternalId() + " -> " + scenario.fulfillmentExternalId(),
            List.of(scenario.salesOrderExternalId(), scenario.fulfillmentExternalId())));
  }
}
