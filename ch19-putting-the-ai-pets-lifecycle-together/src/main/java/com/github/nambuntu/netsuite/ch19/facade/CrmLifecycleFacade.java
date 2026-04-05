package com.github.nambuntu.netsuite.ch19.facade;

import com.github.nambuntu.netsuite.ch19.model.LifecycleEvent;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.model.LifecycleStage;
import java.util.List;

public final class CrmLifecycleFacade {

  public List<LifecycleEvent> run(LifecycleScenario scenario) {
    return List.of(new LifecycleEvent(
        LifecycleStage.CRM,
        scenario.campaignExternalId() + " -> " + scenario.leadExternalId() + " -> " + scenario.customerExternalId(),
        List.of(
            scenario.campaignExternalId(),
            scenario.leadExternalId(),
            scenario.customerExternalId())));
  }
}
