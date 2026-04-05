package com.github.nambuntu.netsuite.ch19.facade;

import com.github.nambuntu.netsuite.ch19.model.LifecycleEvent;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.model.LifecycleStage;
import java.util.List;

public final class SupportLifecycleFacade {

  public List<LifecycleEvent> run(LifecycleScenario scenario) {
    return List.of(new LifecycleEvent(
        LifecycleStage.SUPPORT_AND_RETURN,
        scenario.salesOrderExternalId()
            + " -> "
            + scenario.supportCaseExternalId()
            + " -> "
            + scenario.returnAuthorizationExternalId()
            + " -> "
            + scenario.replacementExternalId(),
        List.of(
            scenario.salesOrderExternalId(),
            scenario.supportCaseExternalId(),
            scenario.returnAuthorizationExternalId(),
            scenario.replacementExternalId())));
  }
}
