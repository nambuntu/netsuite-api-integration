package com.github.nambuntu.netsuite.ch04.model;

public enum IntegrationConcern {
  WRITE("write path"),
  READ("read path"),
  WRITE_AND_READ("write plus later read");

  private final String displayName;

  IntegrationConcern(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
