package com.github.nambuntu.netsuite.ch17.mock;

import com.github.nambuntu.netsuite.ch17.model.CustomFieldDefinition;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordInstance;
import com.github.nambuntu.netsuite.ch17.model.FieldDefinition;
import com.github.nambuntu.netsuite.ch17.model.FieldKind;
import com.github.nambuntu.netsuite.ch17.model.RecordMetadata;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class InMemoryMetadataCatalog {

  public static final String CAMPAIGN_RECORD_TYPE = "campaign";
  public static final String ATTRIBUTION_RECORD_TYPE = "customrecord_campaign_attribution_policy";

  private final Map<String, RecordMetadata> recordMetadataByType;
  private final Map<String, CustomRecordDefinition> customRecordDefinitions;

  public InMemoryMetadataCatalog(
      Map<String, RecordMetadata> recordMetadataByType,
      Map<String, CustomRecordDefinition> customRecordDefinitions) {
    this.recordMetadataByType = Map.copyOf(new LinkedHashMap<>(recordMetadataByType));
    this.customRecordDefinitions = Map.copyOf(new LinkedHashMap<>(customRecordDefinitions));
  }

  public static InMemoryMetadataCatalog createDefault() {
    RecordMetadata campaignMetadata = new RecordMetadata(
        CAMPAIGN_RECORD_TYPE,
        List.of(
            new FieldDefinition("externalId", "External ID", FieldKind.STANDARD_BODY, true),
            new FieldDefinition("title", "Title", FieldKind.STANDARD_BODY, true),
            new FieldDefinition("status", "Status", FieldKind.STANDARD_BODY, true),
            new FieldDefinition("owner", "Owner", FieldKind.STANDARD_BODY, true),
            new FieldDefinition("budget", "Budget", FieldKind.STANDARD_BODY, false)),
        List.of(
            new CustomFieldDefinition(
                "custbody_acquisition_source",
                "Acquisition Source",
                true,
                ATTRIBUTION_RECORD_TYPE),
            new CustomFieldDefinition(
                "custbody_region_bucket",
                "Region Bucket",
                false,
                null),
            new CustomFieldDefinition(
                "custbody_reporting_quarter",
                "Reporting Quarter",
                false,
                null)));

    CustomRecordDefinition attributionDefinition = new CustomRecordDefinition(
        ATTRIBUTION_RECORD_TYPE,
        "Campaign Attribution Policy",
        List.of(
            new CustomRecordInstance("attr-web-launch", "web-launch", "Web Launch"),
            new CustomRecordInstance("attr-partner-referral", "partner-referral", "Partner Referral")));

    return new InMemoryMetadataCatalog(
        Map.of(CAMPAIGN_RECORD_TYPE, campaignMetadata),
        Map.of(ATTRIBUTION_RECORD_TYPE, attributionDefinition));
  }

  public RecordMetadata recordMetadata(String recordType) {
    return Objects.requireNonNull(recordMetadataByType.get(recordType), "record metadata");
  }

  public CustomRecordDefinition customRecordDefinition(String customRecordTypeId) {
    return Objects.requireNonNull(customRecordDefinitions.get(customRecordTypeId), "custom record definition");
  }
}
