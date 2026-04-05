package com.github.nambuntu.netsuite.ch18.model;

public enum CommandStatus {
  RECEIVED("received"),
  IN_PROGRESS("in-progress"),
  COMPLETED("completed"),
  ALREADY_COMPLETED("already-completed"),
  FAILED("failed");

  private final String rendered;

  CommandStatus(String rendered) {
    this.rendered = rendered;
  }

  public String rendered() {
    return rendered;
  }
}
