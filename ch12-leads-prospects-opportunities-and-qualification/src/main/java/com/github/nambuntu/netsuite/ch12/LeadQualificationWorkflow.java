package com.github.nambuntu.netsuite.ch12;

import com.github.nambuntu.netsuite.ch12.command.QualificationCommand;
import com.github.nambuntu.netsuite.ch12.model.CrmParty;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStage;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.model.OpportunityDraft;
import com.github.nambuntu.netsuite.ch12.model.QualifiedCrmState;
import com.github.nambuntu.netsuite.ch12.service.CrmQualificationService;
import com.github.nambuntu.netsuite.ch12.service.OpenOpportunityQueryService;
import java.util.List;
import java.util.Objects;

public final class LeadQualificationWorkflow {

  private final CrmQualificationService crmQualificationService;
  private final OpenOpportunityQueryService openOpportunityQueryService;

  public LeadQualificationWorkflow(
      CrmQualificationService crmQualificationService,
      OpenOpportunityQueryService openOpportunityQueryService) {
    this.crmQualificationService = Objects.requireNonNull(crmQualificationService, "crmQualificationService");
    this.openOpportunityQueryService =
        Objects.requireNonNull(openOpportunityQueryService, "openOpportunityQueryService");
  }

  public QualifiedCrmState qualify(QualificationCommand command) {
    CrmParty lead = crmQualificationService.upsertLead(new CrmParty(
        command.crmPartyExternalId(),
        command.name(),
        command.email(),
        command.campaignExternalId(),
        CrmPartyStage.LEAD,
        command.leadStatus()));

    CrmParty prospect = crmQualificationService.promoteToProspect(
        command.crmPartyExternalId(),
        command.prospectStatus());

    OpenOpportunitySummary opportunity = crmQualificationService.openOpportunity(new OpportunityDraft(
        command.opportunityExternalId(),
        command.crmPartyExternalId(),
        command.opportunityTitle(),
        command.projectedAmount(),
        command.probability(),
        command.expectedCloseDate()));

    List<OpenOpportunitySummary> openOpportunities =
        openOpportunityQueryService.findOpenOpportunities(command.crmPartyExternalId());

    return new QualifiedCrmState(
        command.crmPartyExternalId(),
        lead.stage(),
        prospect.stage(),
        lead.status(),
        prospect.status(),
        opportunity.opportunityExternalId(),
        opportunity.status(),
        opportunity.projectedAmount(),
        opportunity.probability(),
        openOpportunities.size());
  }
}
