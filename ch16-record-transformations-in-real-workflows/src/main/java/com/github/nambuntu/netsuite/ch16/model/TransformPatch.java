package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record TransformPatch(
    String memo,
    String location,
    String supportCaseLink) {

  public List<String> patchedFieldNames() {
    return List.of("memo", "location", "supportCaseLink");
  }
}
