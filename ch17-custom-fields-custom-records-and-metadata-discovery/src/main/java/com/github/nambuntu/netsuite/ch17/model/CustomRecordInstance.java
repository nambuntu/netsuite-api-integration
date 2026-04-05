package com.github.nambuntu.netsuite.ch17.model;

import java.util.Objects;

public record CustomRecordInstance(String internalId, String businessKey, String label) {

  public CustomRecordInstance {
    Objects.requireNonNull(internalId, "internalId");
    Objects.requireNonNull(businessKey, "businessKey");
    Objects.requireNonNull(label, "label");
  }
}
