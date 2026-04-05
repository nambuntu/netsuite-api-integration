package com.github.nambuntu.netsuite.ch19.facade;

import com.github.nambuntu.netsuite.ch19.model.LifecycleEvent;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.model.LifecycleStage;
import java.util.List;

public final class ReturnsLifecycleFacade {

  public List<LifecycleEvent> run(LifecycleScenario scenario) {
    return List.of(new LifecycleEvent(
        LifecycleStage.REFUND_CLOSURE,
        scenario.returnAuthorizationExternalId() + " -> " + scenario.refundExternalId() + " -> case-closed",
        List.of(
            scenario.returnAuthorizationExternalId(),
            scenario.refundExternalId(),
            "case-closed")));
  }
}
