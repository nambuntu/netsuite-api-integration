package com.github.nambuntu.netsuite.ch17.service;

import com.github.nambuntu.netsuite.ch17.mock.InMemoryMetadataCatalog;
import com.github.nambuntu.netsuite.ch17.model.CustomRecordDefinition;
import com.github.nambuntu.netsuite.ch17.model.RecordMetadata;
import java.util.Objects;

public final class MetadataDiscoveryService {

  private final InMemoryMetadataCatalog metadataCatalog;

  public MetadataDiscoveryService(InMemoryMetadataCatalog metadataCatalog) {
    this.metadataCatalog = Objects.requireNonNull(metadataCatalog, "metadataCatalog");
  }

  public RecordMetadata describeRecord(String recordType) {
    try {
      return metadataCatalog.recordMetadata(recordType);
    } catch (NullPointerException exception) {
      throw new MetadataLookupException("Unknown record type in metadata discovery: " + recordType);
    }
  }

  public CustomRecordDefinition describeCustomRecord(String customRecordTypeId) {
    try {
      return metadataCatalog.customRecordDefinition(customRecordTypeId);
    } catch (NullPointerException exception) {
      throw new MetadataLookupException("Unknown custom record type in metadata discovery: " + customRecordTypeId);
    }
  }
}
