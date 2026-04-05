package com.github.nambuntu.netsuite.ch19;

import com.github.nambuntu.netsuite.ch19.facade.CrmLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.OrderLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.ReturnsLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.SupportLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.model.LifecycleResult;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.view.LifecycleTimelineRenderer;

public final class AiPetsLifecycleDemo {

  private AiPetsLifecycleDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  static String renderDemo() {
    AiPetsLifecycleOrchestrator orchestrator = new AiPetsLifecycleOrchestrator(
        new CrmLifecycleFacade(),
        new OrderLifecycleFacade(),
        new SupportLifecycleFacade(),
        new ReturnsLifecycleFacade());
    LifecycleResult result = orchestrator.runScenario(LifecycleScenario.theoScenario());
    return new LifecycleTimelineRenderer().render(result);
  }
}
