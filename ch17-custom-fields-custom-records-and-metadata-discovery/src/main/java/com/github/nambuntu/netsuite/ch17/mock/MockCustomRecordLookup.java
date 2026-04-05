package com.github.nambuntu.netsuite.ch17.mock;

import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordInstance;
import com.github.nambuntu.netsuite.ch17.service.MetadataLookupException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class MockCustomRecordLookup {

  private final Map<String, Map<String, CustomRecordInstance>> recordsByTypeAndBusinessKey;

  public MockCustomRecordLookup(Map<String, CustomRecordDefinition> recordDefinitions) {
    Objects.requireNonNull(recordDefinitions, "recordDefinitions");
    Map<String, Map<String, CustomRecordInstance>> definitions = new LinkedHashMap<>();
    for (Map.Entry<String, CustomRecordDefinition> entry : recordDefinitions.entrySet()) {
      Map<String, CustomRecordInstance> byBusinessKey = new LinkedHashMap<>();
      for (CustomRecordInstance instance : entry.getValue().instances()) {
        byBusinessKey.put(instance.businessKey(), instance);
      }
      definitions.put(entry.getKey(), Map.copyOf(byBusinessKey));
    }
    this.recordsByTypeAndBusinessKey = Map.copyOf(definitions);
  }

  public static MockCustomRecordLookup createDefault() {
    InMemoryMetadataCatalog catalog = InMemoryMetadataCatalog.createDefault();
    return new MockCustomRecordLookup(
        Map.of(InMemoryMetadataCatalog.ATTRIBUTION_RECORD_TYPE,
            catalog.customRecordDefinition(InMemoryMetadataCatalog.ATTRIBUTION_RECORD_TYPE)));
  }

  public CustomRecordInstance resolveByBusinessKey(String customRecordTypeId, String businessKey) {
    Map<String, CustomRecordInstance> byBusinessKey = recordsByTypeAndBusinessKey.get(customRecordTypeId);
    if (byBusinessKey == null) {
      throw new MetadataLookupException("Unknown custom record type: " + customRecordTypeId);
    }
    CustomRecordInstance instance = byBusinessKey.get(businessKey);
    if (instance == null) {
      throw new MetadataLookupException(
          "Unknown custom record business key for " + customRecordTypeId + ": " + businessKey);
    }
    return instance;
  }
}
