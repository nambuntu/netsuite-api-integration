package com.github.nambuntu.netsuite.ch17.mapping;

import java.util.Objects;

public record BaselineFieldMapping(String logicalName, String fieldId) {

  public BaselineFieldMapping {
    Objects.requireNonNull(logicalName, "logicalName");
    Objects.requireNonNull(fieldId, "fieldId");
  }
}
