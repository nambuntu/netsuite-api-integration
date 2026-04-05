package com.github.nambuntu.netsuite.ch19;

import com.github.nambuntu.netsuite.ch19.facade.CrmLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.OrderLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.ReturnsLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.SupportLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.model.LifecycleEvent;
import com.github.nambuntu.netsuite.ch19.model.LifecycleResult;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AiPetsLifecycleOrchestrator {

  private final CrmLifecycleFacade crmLifecycleFacade;
  private final OrderLifecycleFacade orderLifecycleFacade;
  private final SupportLifecycleFacade supportLifecycleFacade;
  private final ReturnsLifecycleFacade returnsLifecycleFacade;

  public AiPetsLifecycleOrchestrator(
      CrmLifecycleFacade crmLifecycleFacade,
      OrderLifecycleFacade orderLifecycleFacade,
      SupportLifecycleFacade supportLifecycleFacade,
      ReturnsLifecycleFacade returnsLifecycleFacade) {
    this.crmLifecycleFacade = Objects.requireNonNull(crmLifecycleFacade, "crmLifecycleFacade");
    this.orderLifecycleFacade = Objects.requireNonNull(orderLifecycleFacade, "orderLifecycleFacade");
    this.supportLifecycleFacade = Objects.requireNonNull(supportLifecycleFacade, "supportLifecycleFacade");
    this.returnsLifecycleFacade = Objects.requireNonNull(returnsLifecycleFacade, "returnsLifecycleFacade");
  }

  public LifecycleResult runScenario(LifecycleScenario scenario) {
    Objects.requireNonNull(scenario, "scenario");
    List<LifecycleEvent> events = new ArrayList<>();
    events.addAll(crmLifecycleFacade.run(scenario));
    events.addAll(orderLifecycleFacade.run(scenario));
    events.addAll(supportLifecycleFacade.run(scenario));
    events.addAll(returnsLifecycleFacade.run(scenario));
    return new LifecycleResult(scenario, events);
  }
}
