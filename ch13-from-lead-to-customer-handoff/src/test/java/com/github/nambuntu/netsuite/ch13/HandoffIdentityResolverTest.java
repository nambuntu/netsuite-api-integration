package com.github.nambuntu.netsuite.ch13;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.nambuntu.netsuite.ch13.model.HandoffIdentity;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffRequest;
import com.github.nambuntu.netsuite.ch13.service.HandoffIdentityResolver;
import org.junit.jupiter.api.Test;

class HandoffIdentityResolverTest {

  private final HandoffIdentityResolver resolver = new HandoffIdentityResolver();

  @Test
  void resolverProducesExpectedRecordSpecificExternalIds() {
    HandoffIdentity identity = resolver.resolve(LeadToCustomerHandoffWorkflowTest.theoRequest());

    assertThat(identity.partyKey()).isEqualTo("party-theo-tran");
    assertThat(identity.leadExternalId()).isEqualTo("lead-theo-tran");
    assertThat(identity.prospectExternalId()).isEqualTo("prospect-theo-tran");
    assertThat(identity.contactExternalId()).isEqualTo("contact-theo-tran");
    assertThat(identity.customerExternalId()).isEqualTo("customer-theo-tran");
  }

  @Test
  void resolverDoesNotLetEmailOverrideConflictingPartyKey() {
    LeadToCustomerHandoffRequest request = new LeadToCustomerHandoffRequest(
        "party-theo-tran",
        "campaign-ai-pets-echo-owl-launch",
        "lead-other-person",
        "prospect-theo-tran",
        "opp-theo-tran-nimbus",
        "contact-theo-tran",
        "customer-theo-tran",
        "Theo Tran",
        "theo@aipets.example",
        "Theo Nimbus Cat first order handoff");

    assertThatThrownBy(() -> resolver.resolve(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("lead external ID");
  }
}
