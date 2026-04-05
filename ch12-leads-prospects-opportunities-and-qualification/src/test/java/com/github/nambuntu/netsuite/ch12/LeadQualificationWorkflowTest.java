package com.github.nambuntu.netsuite.ch12;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch12.command.QualificationCommand;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryCrmPartyStore;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryOpportunityStore;
import com.github.nambuntu.netsuite.ch12.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStatus;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStage;
import com.github.nambuntu.netsuite.ch12.model.QualifiedCrmState;
import com.github.nambuntu.netsuite.ch12.service.CrmQualificationService;
import com.github.nambuntu.netsuite.ch12.service.OpenOpportunityQueryService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LeadQualificationWorkflowTest {

  @Test
  void happyPathQualificationReturnsQualifiedCrmState() {
    LeadQualificationWorkflow workflow = workflow(new InMemoryCrmPartyStore(), new InMemoryOpportunityStore());

    QualifiedCrmState result = workflow.qualify(theoQualificationCommand());

    assertThat(result.crmPartyExternalId()).isEqualTo("crm-theo-tran");
    assertThat(result.stageBefore()).isEqualTo(CrmPartyStage.LEAD);
    assertThat(result.stageAfter()).isEqualTo(CrmPartyStage.PROSPECT);
    assertThat(result.opportunityExternalId()).isEqualTo("opp-theo-nimbus-cat");
    assertThat(result.openOpportunityCount()).isEqualTo(1);
  }

  @Test
  void rerunningQualificationStaysDuplicateSafe() {
    InMemoryCrmPartyStore crmPartyStore = new InMemoryCrmPartyStore();
    InMemoryOpportunityStore opportunityStore = new InMemoryOpportunityStore();
    LeadQualificationWorkflow workflow = workflow(crmPartyStore, opportunityStore);

    workflow.qualify(theoQualificationCommand());
    QualifiedCrmState secondRun = workflow.qualify(theoQualificationCommand());

    assertThat(crmPartyStore.count()).isEqualTo(1);
    assertThat(opportunityStore.count()).isEqualTo(1);
    assertThat(secondRun.openOpportunityCount()).isEqualTo(1);
    assertThat(secondRun.stageAfter()).isEqualTo(CrmPartyStage.PROSPECT);
  }

  private static LeadQualificationWorkflow workflow(
      InMemoryCrmPartyStore crmPartyStore,
      InMemoryOpportunityStore opportunityStore) {
    CrmQualificationService crmQualificationService =
        new CrmQualificationService(crmPartyStore, opportunityStore);
    OpenOpportunityQueryService openOpportunityQueryService =
        new OpenOpportunityQueryService(new MockSuiteQlClient(opportunityStore));
    return new LeadQualificationWorkflow(crmQualificationService, openOpportunityQueryService);
  }

  static QualificationCommand theoQualificationCommand() {
    return new QualificationCommand(
        "crm-theo-tran",
        "Theo Tran",
        "theo@aipets.example",
        "campaign-ai-pets-echo-owl-launch",
        CrmPartyStatus.QUALIFIED,
        CrmPartyStatus.IN_DISCUSSION,
        "opp-theo-nimbus-cat",
        "Theo Nimbus Cat apartment bundle",
        new BigDecimal("1899.00"),
        new BigDecimal("0.65"),
        LocalDate.parse("2026-04-20"));
  }
}
