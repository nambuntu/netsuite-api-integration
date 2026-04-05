package com.github.nambuntu.netsuite.ch13;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch13.model.HandoffContinuityView;
import com.github.nambuntu.netsuite.ch13.service.ContactService;
import com.github.nambuntu.netsuite.ch13.service.CrmStageService;
import com.github.nambuntu.netsuite.ch13.service.CustomerService;
import com.github.nambuntu.netsuite.ch13.service.HandoffContinuityReadService;
import com.github.nambuntu.netsuite.ch13.service.HandoffIdentityResolver;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import com.github.nambuntu.netsuite.ch13.workflow.LeadToCustomerHandoffWorkflow;
import org.junit.jupiter.api.Test;

class HandoffContinuityReadServiceTest {

  @Test
  void continuityViewShowsJoinedStoryAcrossAllRecords() {
    InMemoryHandoffStore store = new InMemoryHandoffStore();
    LeadToCustomerHandoffWorkflow workflow = new LeadToCustomerHandoffWorkflow(
        new HandoffIdentityResolver(),
        new CrmStageService(store),
        new ContactService(store),
        new CustomerService(store),
        new HandoffContinuityReadService(store));

    workflow.handoff(LeadToCustomerHandoffWorkflowTest.theoRequest());

    HandoffContinuityView continuity = new HandoffContinuityReadService(store).readContinuity("party-theo-tran");

    assertThat(continuity.partyKey()).isEqualTo("party-theo-tran");
    assertThat(continuity.leadExternalId()).isEqualTo("lead-theo-tran");
    assertThat(continuity.prospectExternalId()).isEqualTo("prospect-theo-tran");
    assertThat(continuity.opportunityExternalId()).isEqualTo("opp-theo-tran-nimbus");
    assertThat(continuity.contactExternalId()).isEqualTo("contact-theo-tran");
    assertThat(continuity.customerExternalId()).isEqualTo("customer-theo-tran");
    assertThat(continuity.contactEmail()).isEqualTo("theo@aipets.example");
    assertThat(continuity.customerEmail()).isEqualTo("theo@aipets.example");
    assertThat(continuity.continuityStatus()).isEqualTo("OK");
    assertThat(continuity.continuityNote()).isEqualTo(
        "campaign-ai-pets-echo-owl-launch -> opp-theo-tran-nimbus -> customer-theo-tran");
  }
}
