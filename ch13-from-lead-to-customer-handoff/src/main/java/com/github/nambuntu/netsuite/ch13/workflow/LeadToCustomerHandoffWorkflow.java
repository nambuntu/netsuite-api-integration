package com.github.nambuntu.netsuite.ch13.workflow;

import com.github.nambuntu.netsuite.ch13.model.Contact;
import com.github.nambuntu.netsuite.ch13.model.Customer;
import com.github.nambuntu.netsuite.ch13.model.HandoffContinuityView;
import com.github.nambuntu.netsuite.ch13.model.HandoffIdentity;
import com.github.nambuntu.netsuite.ch13.model.Lead;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffRequest;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffResult;
import com.github.nambuntu.netsuite.ch13.model.Opportunity;
import com.github.nambuntu.netsuite.ch13.model.Prospect;
import com.github.nambuntu.netsuite.ch13.service.ContactService;
import com.github.nambuntu.netsuite.ch13.service.CrmStageService;
import com.github.nambuntu.netsuite.ch13.service.CustomerService;
import com.github.nambuntu.netsuite.ch13.service.HandoffContinuityReadService;
import com.github.nambuntu.netsuite.ch13.service.HandoffIdentityResolver;
import java.util.Objects;

public final class LeadToCustomerHandoffWorkflow {

  private final HandoffIdentityResolver identityResolver;
  private final CrmStageService crmStageService;
  private final ContactService contactService;
  private final CustomerService customerService;
  private final HandoffContinuityReadService handoffReadService;

  public LeadToCustomerHandoffWorkflow(
      HandoffIdentityResolver identityResolver,
      CrmStageService crmStageService,
      ContactService contactService,
      CustomerService customerService,
      HandoffContinuityReadService handoffReadService) {
    this.identityResolver = Objects.requireNonNull(identityResolver, "identityResolver");
    this.crmStageService = Objects.requireNonNull(crmStageService, "crmStageService");
    this.contactService = Objects.requireNonNull(contactService, "contactService");
    this.customerService = Objects.requireNonNull(customerService, "customerService");
    this.handoffReadService = Objects.requireNonNull(handoffReadService, "handoffReadService");
  }

  public LeadToCustomerHandoffResult handoff(LeadToCustomerHandoffRequest request) {
    HandoffIdentity identity = identityResolver.resolve(request);

    crmStageService.upsertLead(new Lead(
        identity.leadExternalId(),
        identity.partyKey(),
        request.campaignExternalId(),
        request.primaryEmail()));
    crmStageService.upsertProspect(new Prospect(
        identity.prospectExternalId(),
        identity.partyKey(),
        request.campaignExternalId(),
        request.primaryEmail()));
    crmStageService.upsertOpportunity(new Opportunity(
        identity.opportunityExternalId(),
        identity.partyKey(),
        request.campaignExternalId(),
        request.opportunityTitle()));

    contactService.upsertContact(new Contact(
        identity.contactExternalId(),
        identity.partyKey(),
        request.primaryEmail(),
        identity.customerExternalId()));
    customerService.upsertCustomer(new Customer(
        identity.customerExternalId(),
        identity.partyKey(),
        request.primaryEmail(),
        request.campaignExternalId(),
        identity.opportunityExternalId()));

    HandoffContinuityView continuity = handoffReadService.readContinuity(identity.partyKey());
    return LeadToCustomerHandoffResult.from(identity, continuity);
  }
}
