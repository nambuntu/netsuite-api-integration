package com.github.nambuntu.netsuite.ch13;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffRequest;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffResult;
import com.github.nambuntu.netsuite.ch13.service.ContactService;
import com.github.nambuntu.netsuite.ch13.service.CrmStageService;
import com.github.nambuntu.netsuite.ch13.service.CustomerService;
import com.github.nambuntu.netsuite.ch13.service.HandoffContinuityReadService;
import com.github.nambuntu.netsuite.ch13.service.HandoffIdentityResolver;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import com.github.nambuntu.netsuite.ch13.workflow.LeadToCustomerHandoffWorkflow;
import org.junit.jupiter.api.Test;

class LeadToCustomerHandoffWorkflowTest {

  @Test
  void happyPathCreatesJoinedContinuityStory() {
    InMemoryHandoffStore store = new InMemoryHandoffStore();
    LeadToCustomerHandoffWorkflow workflow = workflow(store);

    LeadToCustomerHandoffResult result = workflow.handoff(theoRequest());

    assertThat(result.handoffStatus()).isEqualTo("SUCCESS");
    assertThat(result.partyKey()).isEqualTo("party-theo-tran");
    assertThat(result.customerExternalId()).isEqualTo("customer-theo-tran");
    assertThat(result.continuityStatus()).isEqualTo("OK");
  }

  @Test
  void repeatRunDoesNotCreateDuplicateContactOrCustomer() {
    InMemoryHandoffStore store = new InMemoryHandoffStore();
    LeadToCustomerHandoffWorkflow workflow = workflow(store);

    workflow.handoff(theoRequest());
    LeadToCustomerHandoffResult second = workflow.handoff(theoRequest());

    assertThat(store.countContacts()).isEqualTo(1);
    assertThat(store.countCustomers()).isEqualTo(1);
    assertThat(second.notes()).isEqualTo("campaign-ai-pets-echo-owl-launch -> opp-theo-tran-nimbus -> customer-theo-tran");
  }

  private static LeadToCustomerHandoffWorkflow workflow(InMemoryHandoffStore store) {
    return new LeadToCustomerHandoffWorkflow(
        new HandoffIdentityResolver(),
        new CrmStageService(store),
        new ContactService(store),
        new CustomerService(store),
        new HandoffContinuityReadService(store));
  }

  static LeadToCustomerHandoffRequest theoRequest() {
    return new LeadToCustomerHandoffRequest(
        "party-theo-tran",
        "campaign-ai-pets-echo-owl-launch",
        "lead-theo-tran",
        "prospect-theo-tran",
        "opp-theo-tran-nimbus",
        "contact-theo-tran",
        "customer-theo-tran",
        "Theo Tran",
        "theo@aipets.example",
        "Theo Nimbus Cat first order handoff");
  }
}
