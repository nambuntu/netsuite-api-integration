package com.github.nambuntu.netsuite.ch17.model;

import java.util.List;
import java.util.Objects;

public record CustomRecordDefinition(String recordTypeId, String label, List<CustomRecordInstance> instances) {

  public CustomRecordDefinition {
    Objects.requireNonNull(recordTypeId, "recordTypeId");
    Objects.requireNonNull(label, "label");
    instances = List.copyOf(instances);
  }
}
