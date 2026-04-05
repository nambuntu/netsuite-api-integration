package com.github.nambuntu.netsuite.ch04.model;

import java.util.Objects;

public record RecordVocabularyEntry(
    BusinessMoment moment,
    NetSuiteRecordTypeName recordType,
    BusinessStateMeaning stateMeaning,
    IntegrationConcern integrationConcern,
    IdentifierGuidance identifierGuidance) {

  public RecordVocabularyEntry {
    moment = Objects.requireNonNull(moment, "moment must not be null");
    recordType = Objects.requireNonNull(recordType, "recordType must not be null");
    stateMeaning = Objects.requireNonNull(stateMeaning, "stateMeaning must not be null");
    integrationConcern = Objects.requireNonNull(integrationConcern, "integrationConcern must not be null");
    identifierGuidance = Objects.requireNonNull(identifierGuidance, "identifierGuidance must not be null");
  }

  public String teachingLine() {
    return moment.name()
        + " [" + moment.scenarioLabel() + "]"
        + " -> " + recordType.apiName()
        + " -> " + stateMeaning.displayName()
        + " -> " + integrationConcern.displayName()
        + " -> key: " + identifierGuidance.summary();
  }
}
