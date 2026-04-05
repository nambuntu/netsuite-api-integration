package com.github.nambuntu.netsuite.ch17;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch17.mock.InMemoryMetadataCatalog;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.RecordMetadata;
import com.github.nambuntu.netsuite.ch17.service.MetadataDiscoveryService;
import org.junit.jupiter.api.Test;

class MetadataDiscoveryServiceTest {

  @Test
  void describeRecordReturnsStandardCustomAndCustomRecordMetadata() {
    MetadataDiscoveryService service = new MetadataDiscoveryService(InMemoryMetadataCatalog.createDefault());

    RecordMetadata metadata = service.describeRecord("campaign");
    CustomRecordDefinition definition = service.describeCustomRecord("customrecord_campaign_attribution_policy");

    assertThat(metadata.standardFieldCount()).isEqualTo(5);
    assertThat(metadata.customFieldCount()).isEqualTo(3);
    assertThat(metadata.standardFields()).extracting(field -> field.fieldId())
        .containsExactly("externalId", "title", "status", "owner", "budget");
    assertThat(metadata.customFields()).extracting(field -> field.fieldId())
        .containsExactly(
            "custbody_acquisition_source",
            "custbody_region_bucket",
            "custbody_reporting_quarter");
    assertThat(definition.instances()).extracting(instance -> instance.internalId())
        .containsExactly("attr-web-launch", "attr-partner-referral");
  }
}
