package com.github.nambuntu.netsuite.ch17.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;

public record MappedCampaignPayload(
    String recordType,
    String resolvedAttributionReference,
    Map<String, Object> outboundFields) {

  public MappedCampaignPayload {
    Objects.requireNonNull(recordType, "recordType");
    outboundFields = Collections.unmodifiableMap(new LinkedHashMap<>(outboundFields));
  }

  public List<String> mappedFieldIds() {
    return outboundFields.keySet().stream().toList();
  }
}
