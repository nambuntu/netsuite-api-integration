package com.github.nambuntu.netsuite.ch04.model;

public enum BusinessStateMeaning {
  RELATIONSHIP_STATE("relationship state"),
  PRE_ORDER_SALES_STATE("pre-order sales state"),
  COMMITMENT_STATE("commitment state"),
  BILLABLE_STATE("billable state"),
  SETTLEMENT_STATE("settlement state"),
  OPERATIONAL_STATE("operational state"),
  POST_SALE_SUPPORT_STATE("post-sale support state"),
  CONTROLLED_REVERSE_FLOW_STATE("controlled reverse-flow state");

  private final String displayName;

  BusinessStateMeaning(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
