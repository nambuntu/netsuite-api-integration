package com.github.nambuntu.netsuite.ch12;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch12.mock.InMemoryCrmPartyStore;
import com.github.nambuntu.netsuite.ch12.mock.InMemoryOpportunityStore;
import com.github.nambuntu.netsuite.ch12.model.CrmParty;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStage;
import com.github.nambuntu.netsuite.ch12.model.CrmPartyStatus;
import com.github.nambuntu.netsuite.ch12.service.CrmQualificationService;
import org.junit.jupiter.api.Test;

class CrmQualificationServiceTest {

  @Test
  void sameExternalIdSurvivesPromotionWithoutCreatingSecondParty() {
    InMemoryCrmPartyStore crmPartyStore = new InMemoryCrmPartyStore();
    CrmQualificationService service =
        new CrmQualificationService(crmPartyStore, new InMemoryOpportunityStore());

    service.upsertLead(new CrmParty(
        "crm-theo-tran",
        "Theo Tran",
        "theo@aipets.example",
        "campaign-ai-pets-echo-owl-launch",
        CrmPartyStage.LEAD,
        CrmPartyStatus.QUALIFIED));
    CrmParty prospect = service.promoteToProspect("crm-theo-tran", CrmPartyStatus.IN_DISCUSSION);

    assertThat(crmPartyStore.count()).isEqualTo(1);
    assertThat(prospect.externalId()).isEqualTo("crm-theo-tran");
    assertThat(prospect.stage()).isEqualTo(CrmPartyStage.PROSPECT);
    assertThat(prospect.stage()).isNotEqualTo(CrmPartyStage.CUSTOMER);
  }
}
