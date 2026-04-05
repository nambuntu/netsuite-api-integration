package com.github.nambuntu.netsuite.ch12;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch12.mock.InMemoryOpportunityStore;
import com.github.nambuntu.netsuite.ch12.mock.MockSuiteQlClient;
import com.github.nambuntu.netsuite.ch12.model.OpenOpportunitySummary;
import com.github.nambuntu.netsuite.ch12.model.OpportunityStatus;
import com.github.nambuntu.netsuite.ch12.service.OpenOpportunityQueryService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class OpenOpportunityQueryServiceTest {

  @Test
  void queryReturnsExpectedOpenOpportunitySummaryWithoutLeakage() {
    InMemoryOpportunityStore store = new InMemoryOpportunityStore();
    store.save(new OpenOpportunitySummary(
        "opp-theo-nimbus-cat",
        "crm-theo-tran",
        OpportunityStatus.OPEN,
        new BigDecimal("1899.00"),
        new BigDecimal("0.65"),
        LocalDate.parse("2026-04-20")));
    store.save(new OpenOpportunitySummary(
        "opp-ava-echo-owl",
        "crm-ava-cole",
        OpportunityStatus.OPEN,
        new BigDecimal("999.00"),
        new BigDecimal("0.40"),
        LocalDate.parse("2026-04-22")));
    store.save(new OpenOpportunitySummary(
        "opp-theo-old-closed",
        "crm-theo-tran",
        OpportunityStatus.CLOSED_LOST,
        new BigDecimal("499.00"),
        new BigDecimal("0.15"),
        LocalDate.parse("2026-03-30")));

    OpenOpportunityQueryService service = new OpenOpportunityQueryService(new MockSuiteQlClient(store));

    assertThat(service.findOpenOpportunities("crm-theo-tran"))
        .singleElement()
        .satisfies(summary -> {
          assertThat(summary.opportunityExternalId()).isEqualTo("opp-theo-nimbus-cat");
          assertThat(summary.crmPartyExternalId()).isEqualTo("crm-theo-tran");
          assertThat(summary.status()).isEqualTo(OpportunityStatus.OPEN);
        });
  }
}
