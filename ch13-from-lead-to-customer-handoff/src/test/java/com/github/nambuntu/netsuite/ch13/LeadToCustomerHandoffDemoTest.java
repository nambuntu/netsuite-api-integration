package com.github.nambuntu.netsuite.ch13;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadToCustomerHandoffDemoTest {

  @Test
  void renderDemoPrintsCompactHandoffSummary() {
    assertThat(LeadToCustomerHandoffDemo.renderDemo()).isEqualTo("""
        handoff: SUCCESS
        partyKey: party-theo-tran
        lead: lead-theo-tran
        prospect: prospect-theo-tran
        opportunity: opp-theo-tran-nimbus
        contact: contact-theo-tran
        customer: customer-theo-tran
        continuity: OK
        notes: campaign-ai-pets-echo-owl-launch -> opp-theo-tran-nimbus -> customer-theo-tran""");
  }
}
