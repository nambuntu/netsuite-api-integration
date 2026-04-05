package com.github.nambuntu.netsuite.ch19;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch19.facade.CrmLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.OrderLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.ReturnsLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.facade.SupportLifecycleFacade;
import com.github.nambuntu.netsuite.ch19.model.LifecycleResult;
import com.github.nambuntu.netsuite.ch19.model.LifecycleScenario;
import com.github.nambuntu.netsuite.ch19.model.LifecycleStage;
import org.junit.jupiter.api.Test;

class AiPetsLifecycleOrchestratorTest {

  @Test
  void runScenarioPreservesContinuityFromCampaignThroughRefund() {
    AiPetsLifecycleOrchestrator orchestrator = new AiPetsLifecycleOrchestrator(
        new CrmLifecycleFacade(),
        new OrderLifecycleFacade(),
        new SupportLifecycleFacade(),
        new ReturnsLifecycleFacade());

    LifecycleResult result = orchestrator.runScenario(LifecycleScenario.theoScenario());

    assertThat(result.events()).hasSize(5);
    assertThat(result.events()).extracting(event -> event.stage()).containsExactly(
        LifecycleStage.CRM,
        LifecycleStage.ORDER_AND_BILLING,
        LifecycleStage.FULFILLMENT,
        LifecycleStage.SUPPORT_AND_RETURN,
        LifecycleStage.REFUND_CLOSURE);
    assertThat(result.events().get(0).linkedIds())
        .containsExactly("campaign-ai-pets-launch", "lead-theo-tran", "customer-theo-tran");
    assertThat(result.events().get(1).linkedIds())
        .contains("customer-theo-tran", "so-theo-1001", "inv-theo-1001", "payment-theo-1001");
    assertThat(result.events().get(2).linkedIds())
        .containsExactly("so-theo-1001", "fulfill-theo-1001");
    assertThat(result.events().get(3).linkedIds())
        .containsExactly("so-theo-1001", "case-theo-color-mismatch", "ra-theo-1001", "replacement-theo-1001");
    assertThat(result.events().get(4).linkedIds())
        .containsExactly("ra-theo-1001", "refund-theo-1001", "case-closed");
  }
}
