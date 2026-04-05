package com.github.nambuntu.netsuite.ch19;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch19.facade.CrmLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.OrderLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.ReturnsLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.SupportLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.view.LifecycleTimelineRenderer;
import org.junit.jupiter.api.Test;

class LifecycleTimelineRendererTest {

  @Test
  void renderPrintsTheFiveLineLifecycleStoryInBusinessOrder() {
    AiPetsLifecycleOrchestrator orchestrator = new AiPetsLifecycleOrchestrator(
        new CrmLifecycleFacade(),
        new OrderLifecycleFacade(),
        new SupportLifecycleFacade(),
        new ReturnsLifecycleFacade());

    String rendered = new LifecycleTimelineRenderer().render(
        orchestrator.runScenario(LifecycleScenario.theoScenario()));

    assertThat(rendered).isEqualTo("""
        campaign-ai-pets-launch -> lead-theo-tran -> customer-theo-tran
        customer-theo-tran -> so-theo-1001 -> inv-theo-1001 -> payment-theo-1001
        so-theo-1001 -> fulfill-theo-1001
        so-theo-1001 -> case-theo-color-mismatch -> ra-theo-1001 -> replacement-theo-1001
        ra-theo-1001 -> refund-theo-1001 -> case-closed""");
  }
}
