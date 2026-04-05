package com.github.nambuntu.netsuite.ch12;

import com.github.nambuntu.netsuite.ch12.command.QualificationCommand;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryCrmPartyStore;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryOpportunityStore;
import com.github.nambuntu.netsuite.ch12.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStatus;
import com.github.nambuntu.netsuite.ch12.model.QualifiedCrmState;
import com.github.nambuntu.netsuite.ch12.service.CrmQualificationService;
import com.github.nambuntu.netsuite.ch12.service.OpenOpportunityQueryService;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class QualificationDemo {

  private QualificationDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    InMemoryCrmPartyStore crmPartyStore = new InMemoryCrmPartyStore();
    InMemoryOpportunityStore opportunityStore = new InMemoryOpportunityStore();
    CrmQualificationService crmQualificationService =
        new CrmQualificationService(crmPartyStore, opportunityStore);
    OpenOpportunityQueryService openOpportunityQueryService =
        new OpenOpportunityQueryService(new MockSuiteQlClient(opportunityStore));
    LeadQualificationWorkflow workflow =
        new LeadQualificationWorkflow(crmQualificationService, openOpportunityQueryService);

    QualifiedCrmState result = workflow.qualify(new QualificationCommand(
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
        LocalDate.parse("2026-04-20")));

    return """
        CRM qualification result
        party: %s
        stage: %s -> %s
        status: %s -> %s
        opportunity: %s
        amount: %s
        probability: %s
        open opportunities for %s: %d
        """.formatted(
        result.crmPartyExternalId(),
        result.stageBefore(),
        result.stageAfter(),
        result.statusBefore(),
        result.statusAfter(),
        result.opportunityExternalId(),
        result.projectedAmount().setScale(2).toPlainString(),
        result.probability().setScale(2).toPlainString(),
        result.crmPartyExternalId(),
        result.openOpportunityCount()).stripTrailing();
  }
}
