package com.github.nambuntu.netsuite.ch17;

import com.github.nambuntu.netsuite.ch17.mapping.FieldMappingRegistry;
import com.github.nambuntu.netsuite.ch17.mock.InMemoryMetadataCatalog;
import com.github.nambuntu.netsuite.ch17.mock.MockCustomRecordLookup;
import com.github.nambuntu.netsuite.ch17.model.CampaignAttributionInput;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.MappedCampaignPayload;
import com.github.nambuntu.netsuite.ch17.model.RecordMetadata;
import com.github.nambuntu.netsuite.ch17.service.CampaignAttributionMapper;
import com.github.nambuntu.netsuite.ch17.service.MetadataDiscoveryService;
import com.github.nambuntu.netsuite.ch17.service.RequiredFieldValidator;
import java.math.BigDecimal;
import java.util.StringJoiner;

public final class MetadataDiscoveryDemo {

  private MetadataDiscoveryDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  static String renderDemo() {
    InMemoryMetadataCatalog metadataCatalog = InMemoryMetadataCatalog.createDefault();
    MetadataDiscoveryService discoveryService = new MetadataDiscoveryService(metadataCatalog);
    RequiredFieldValidator validator = new RequiredFieldValidator();
    CampaignAttributionMapper mapper = new CampaignAttributionMapper(MockCustomRecordLookup.createDefault());

    RecordMetadata metadata = discoveryService.describeRecord(InMemoryMetadataCatalog.CAMPAIGN_RECORD_TYPE);
    CustomRecordDefinition attributionDefinition = discoveryService.describeCustomRecord(
        InMemoryMetadataCatalog.ATTRIBUTION_RECORD_TYPE);
    FieldMappingRegistry registry = FieldMappingRegistry.fromMetadata(metadata, attributionDefinition);

    CampaignAttributionInput validInput = new CampaignAttributionInput(
        "campaign-ai-pets-launch",
        "AI-Pets Spring Launch",
        "ACTIVE",
        "ava-ops",
        new BigDecimal("12500.00"),
        "web-launch",
        "region-eu-central",
        "2026-Q2");

    validator.validate(validInput, registry);
    MappedCampaignPayload payload = mapper.map(validInput, registry);

    StringJoiner joiner = new StringJoiner(System.lineSeparator());
    joiner.add("record: " + metadata.recordType());
    joiner.add("standard fields discovered: " + metadata.standardFieldCount());
    joiner.add("custom fields discovered: " + metadata.customFieldCount());
    joiner.add("custom record types discovered: 1");
    joiner.add("required extension fields present: yes");
    joiner.add("resolved attribution reference: " + payload.resolvedAttributionReference());
    joiner.add("mapped outbound fields:");
    payload.mappedFieldIds().forEach(fieldId -> joiner.add("- " + fieldId));
    return joiner.toString();
  }
}
