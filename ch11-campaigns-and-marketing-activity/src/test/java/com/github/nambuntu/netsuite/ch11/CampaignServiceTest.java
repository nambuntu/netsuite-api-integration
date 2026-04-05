package com.github.nambuntu.netsuite.ch11;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch11.mock.InMemoryCampaignStore;
import com.github.nambuntu.netsuite.ch11.model.Campaign;
import com.github.nambuntu.netsuite.ch11.model.CampaignChannel;
import com.github.nambuntu.netsuite.ch11.model.CampaignStatus;
import com.github.nambuntu.netsuite.ch11.service.CampaignService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CampaignServiceTest {

  @Test
  void upsertKeepsCampaignExternalIdentityAndUpdatesFields() {
    CampaignService service = new CampaignService(new InMemoryCampaignStore());

    service.upsertCampaign(new Campaign(
        "campaign-ai-pets-echo-owl-launch",
        "Echo Owl launch",
        CampaignChannel.EMAIL,
        CampaignStatus.ACTIVE,
        new BigDecimal("2500.00")));
    service.upsertCampaign(new Campaign(
        "campaign-ai-pets-echo-owl-launch",
        "Echo Owl launch refreshed",
        CampaignChannel.EMAIL,
        CampaignStatus.ACTIVE,
        new BigDecimal("3200.00")));

    assertThat(service.findCampaignByExternalId("campaign-ai-pets-echo-owl-launch"))
        .hasValueSatisfying(campaign -> {
          assertThat(campaign.externalId()).isEqualTo("campaign-ai-pets-echo-owl-launch");
          assertThat(campaign.name()).isEqualTo("Echo Owl launch refreshed");
          assertThat(campaign.budget()).isEqualByComparingTo("3200.00");
        });
  }
}
