package com.github.nambuntu.netsuite.ch17;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.nambuntu.netsuite.ch17.mapping.FieldMappingRegistry;
import com.github.nambuntu.netsuite.ch17.mock.InMemoryMetadataCatalog;
import com.github.nambuntu.netsuite.ch17.mock.MockCustomRecordLookup;
import com.github.nambuntu.netsuite.ch17.model.CampaignAttributionInput;
import com.github.nambuntu.netsuite.ch17.model.MappedCampaignPayload;
import com.github.nambuntu.netsuite.ch17.service.CampaignAttributionMapper;
import com.github.nambuntu.netsuite.ch17.service.MetadataDiscoveryService;
import com.github.nambuntu.netsuite.ch17.service.MetadataLookupException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CampaignAttributionMapperTest {

  @Test
  void mapUsesDiscoveredFieldIdsAndResolvedCustomRecordReference() {
    InMemoryMetadataCatalog catalog = InMemoryMetadataCatalog.createDefault();
    MetadataDiscoveryService discoveryService = new MetadataDiscoveryService(catalog);
    FieldMappingRegistry registry = FieldMappingRegistry.fromMetadata(
        discoveryService.describeRecord("campaign"),
        discoveryService.describeCustomRecord("customrecord_campaign_attribution_policy"));
    CampaignAttributionMapper mapper = new CampaignAttributionMapper(MockCustomRecordLookup.createDefault());

    MappedCampaignPayload payload = mapper.map(
        new CampaignAttributionInput(
            "campaign-ai-pets-launch",
            "AI-Pets Spring Launch",
            "ACTIVE",
            "ava-ops",
            new BigDecimal("12500.00"),
            "web-launch",
            "region-eu-central",
            "2026-Q2"),
        registry);

    assertThat(payload.resolvedAttributionReference()).isEqualTo("attr-web-launch");
    assertThat(payload.outboundFields()).containsEntry("custbody_acquisition_source", "attr-web-launch");
    assertThat(payload.outboundFields()).containsEntry("custbody_region_bucket", "region-eu-central");
    assertThat(payload.outboundFields()).containsEntry("custbody_reporting_quarter", "2026-Q2");
    assertThat(payload.mappedFieldIds()).containsExactly(
        "externalId",
        "title",
        "status",
        "owner",
        "budget",
        "custbody_acquisition_source",
        "custbody_region_bucket",
        "custbody_reporting_quarter");
  }

  @Test
  void mapFailsClearlyForUnknownAttributionKey() {
    InMemoryMetadataCatalog catalog = InMemoryMetadataCatalog.createDefault();
    MetadataDiscoveryService discoveryService = new MetadataDiscoveryService(catalog);
    FieldMappingRegistry registry = FieldMappingRegistry.fromMetadata(
        discoveryService.describeRecord("campaign"),
        discoveryService.describeCustomRecord("customrecord_campaign_attribution_policy"));
    CampaignAttributionMapper mapper = new CampaignAttributionMapper(MockCustomRecordLookup.createDefault());

    assertThatThrownBy(() -> mapper.map(
        new CampaignAttributionInput(
            "campaign-ai-pets-launch",
            "AI-Pets Spring Launch",
            "ACTIVE",
            "ava-ops",
            new BigDecimal("12500.00"),
            "unknown-source",
            "region-eu-central",
            "2026-Q2"),
        registry))
        .isInstanceOf(MetadataLookupException.class)
        .hasMessageContaining("Unknown custom record business key");
  }
}
