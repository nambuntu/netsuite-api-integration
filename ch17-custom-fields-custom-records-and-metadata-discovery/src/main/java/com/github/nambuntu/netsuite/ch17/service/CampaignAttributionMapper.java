package com.github.nambuntu.netsuite.ch17.service;

import com.github.nambuntu.netsuite.ch17.mapping.ExtensionFieldMapping;
import com.github.nambuntu.netsuite.ch17.mapping.FieldMappingRegistry;
import com.github.nambuntu.netsuite.ch17.mock.MockCustomRecordLookup;
import com.github.nambuntu.netsuite.ch17.model.CampaignAttributionInput;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordInstance;
import com.github.nambuntu.netsuite.ch17.model.MappedCampaignPayload;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class CampaignAttributionMapper {

  private final MockCustomRecordLookup customRecordLookup;

  public CampaignAttributionMapper(MockCustomRecordLookup customRecordLookup) {
    this.customRecordLookup = Objects.requireNonNull(customRecordLookup, "customRecordLookup");
  }

  public MappedCampaignPayload map(CampaignAttributionInput input, FieldMappingRegistry registry) {
    Objects.requireNonNull(input, "input");
    Objects.requireNonNull(registry, "registry");

    Map<String, Object> outboundFields = new LinkedHashMap<>();
    outboundFields.put(registry.baselineFieldId("externalId"), input.externalId());
    outboundFields.put(registry.baselineFieldId("title"), input.title());
    outboundFields.put(registry.baselineFieldId("status"), input.status());
    outboundFields.put(registry.baselineFieldId("owner"), input.owner());
    outboundFields.put(registry.baselineFieldId("budget"), input.budget());

    ExtensionFieldMapping acquisitionSource = registry.extension("acquisitionSource");
    CustomRecordInstance resolvedAttribution = customRecordLookup.resolveByBusinessKey(
        acquisitionSource.customRecordTypeId(),
        input.acquisitionSourceKey());
    outboundFields.put(acquisitionSource.fieldId(), resolvedAttribution.internalId());

    if (input.regionBucket() != null && !input.regionBucket().isBlank()) {
      outboundFields.put(registry.extension("regionBucket").fieldId(), input.regionBucket());
    }
    if (input.reportingQuarter() != null && !input.reportingQuarter().isBlank()) {
      outboundFields.put(registry.extension("reportingQuarter").fieldId(), input.reportingQuarter());
    }

    return new MappedCampaignPayload(
        "campaign",
        resolvedAttribution.internalId(),
        outboundFields);
  }
}
