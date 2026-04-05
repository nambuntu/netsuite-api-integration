package com.github.nambuntu.netsuite.ch12;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QualificationDemoTest {

  @Test
  void renderDemoPrintsCompactQualificationSummary() {
    assertThat(QualificationDemo.renderDemo()).isEqualTo("""
        CRM qualification result
        party: crm-theo-tran
        stage: LEAD -> PROSPECT
        status: QUALIFIED -> IN_DISCUSSION
        opportunity: opp-theo-nimbus-cat
        amount: 1899.00
        probability: 0.65
        open opportunities for crm-theo-tran: 1""");
  }
}
