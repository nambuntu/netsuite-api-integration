package com.github.nambuntu.netsuite.ch17.mapping;

import com.github.nambuntu.netsuite.ch17.model.CustomFieldDefinition;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.RecordMetadata;
import com.github.nambuntu.netsuite.ch17.service.MetadataLookupException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class FieldMappingRegistry {

  private final Map<String, BaselineFieldMapping> baselineMappings;
  private final Map<String, ExtensionFieldMapping> extensionMappings;
  private final CustomRecordDefinition attributionDefinition;

  private FieldMappingRegistry(
      Map<String, BaselineFieldMapping> baselineMappings,
      Map<String, ExtensionFieldMapping> extensionMappings,
      CustomRecordDefinition attributionDefinition) {
    this.baselineMappings = Collections.unmodifiableMap(new LinkedHashMap<>(baselineMappings));
    this.extensionMappings = Collections.unmodifiableMap(new LinkedHashMap<>(extensionMappings));
    this.attributionDefinition = Objects.requireNonNull(attributionDefinition, "attributionDefinition");
  }

  public static FieldMappingRegistry fromMetadata(
      RecordMetadata metadata,
      CustomRecordDefinition attributionDefinition) {
    Objects.requireNonNull(metadata, "metadata");
    Objects.requireNonNull(attributionDefinition, "attributionDefinition");

    Map<String, BaselineFieldMapping> baselineMappings = new LinkedHashMap<>();
    baselineMappings.put("externalId", new BaselineFieldMapping("externalId", "externalId"));
    baselineMappings.put("title", new BaselineFieldMapping("title", "title"));
    baselineMappings.put("status", new BaselineFieldMapping("status", "status"));
    baselineMappings.put("owner", new BaselineFieldMapping("owner", "owner"));
    baselineMappings.put("budget", new BaselineFieldMapping("budget", "budget"));

    Map<String, ExtensionFieldMapping> extensionMappings = new LinkedHashMap<>();
    extensionMappings.put(
        "acquisitionSource",
        extensionFrom(metadata, "custbody_acquisition_source", "acquisitionSource"));
    extensionMappings.put(
        "regionBucket",
        extensionFrom(metadata, "custbody_region_bucket", "regionBucket"));
    extensionMappings.put(
        "reportingQuarter",
        extensionFrom(metadata, "custbody_reporting_quarter", "reportingQuarter"));

    ExtensionFieldMapping acquisitionSource = extensionMappings.get("acquisitionSource");
    if (!attributionDefinition.recordTypeId().equals(acquisitionSource.customRecordTypeId())) {
      throw new MetadataLookupException("Discovered attribution field is not backed by the expected custom record.");
    }

    return new FieldMappingRegistry(baselineMappings, extensionMappings, attributionDefinition);
  }

  private static ExtensionFieldMapping extensionFrom(
      RecordMetadata metadata,
      String fieldId,
      String logicalName) {
    CustomFieldDefinition field = metadata.findCustomField(fieldId)
        .orElseThrow(() -> new MetadataLookupException("Missing custom field in discovered metadata: " + fieldId));
    return new ExtensionFieldMapping(logicalName, field.fieldId(), field.required(), field.customRecordTypeId());
  }

  public List<BaselineFieldMapping> baselineMappings() {
    return baselineMappings.values().stream().toList();
  }

  public List<ExtensionFieldMapping> extensionMappings() {
    return extensionMappings.values().stream().toList();
  }

  public String baselineFieldId(String logicalName) {
    BaselineFieldMapping mapping = baselineMappings.get(logicalName);
    if (mapping == null) {
      throw new MetadataLookupException("Unknown baseline mapping: " + logicalName);
    }
    return mapping.fieldId();
  }

  public ExtensionFieldMapping extension(String logicalName) {
    ExtensionFieldMapping mapping = extensionMappings.get(logicalName);
    if (mapping == null) {
      throw new MetadataLookupException("Unknown extension mapping: " + logicalName);
    }
    return mapping;
  }

  public CustomRecordDefinition attributionDefinition() {
    return attributionDefinition;
  }
}
