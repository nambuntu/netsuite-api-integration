package com.github.nambuntu.netsuite.ch13;

import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffRequest;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffResult;
import com.github.nambuntu.netsuite.ch13.service.ContactService;
import com.github.nambuntu.netsuite.ch13.service.CrmStageService;
import com.github.nambuntu.netsuite.ch13.service.CustomerService;
import com.github.nambuntu.netsuite.ch13.service.HandoffContinuityReadService;
import com.github.nambuntu.netsuite.ch13.service.HandoffIdentityResolver;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import com.github.nambuntu.netsuite.ch13.workflow.LeadToCustomerHandoffWorkflow;

public final class LeadToCustomerHandoffDemo {

  private LeadToCustomerHandoffDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    InMemoryHandoffStore store = new InMemoryHandoffStore();
    LeadToCustomerHandoffWorkflow workflow = new LeadToCustomerHandoffWorkflow(
        new HandoffIdentityResolver(),
        new CrmStageService(store),
        new ContactService(store),
        new CustomerService(store),
        new HandoffContinuityReadService(store));

    LeadToCustomerHandoffResult result = workflow.handoff(new LeadToCustomerHandoffRequest(
        "party-theo-tran",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        "prospect-theo-tran",
        "opp-theo-tran-nimbus",
        "contact-theo-tran",
        "customer-theo-tran",
        "Theo Tran",
        "theo@aipets.example",
        "Theo Nimbus Cat first order handoff"));

    return """
        handoff: %s
        partyKey: %s
        lead: %s
        prospect: %s
        opportunity: %s
        contact: %s
        customer: %s
        continuity: %s
        notes: %s
        """.formatted(
        result.handoffStatus(),
        result.partyKey(),
        result.leadExternalId(),
        result.prospectExternalId(),
        result.opportunityExternalId(),
        result.contactExternalId(),
        result.customerExternalId(),
        result.continuityStatus(),
        result.notes()).stripTrailing();
  }
}
