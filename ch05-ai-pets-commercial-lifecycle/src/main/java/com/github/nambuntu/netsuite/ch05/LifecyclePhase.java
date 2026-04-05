package com.github.nambuntu.netsuite.ch05;

public enum LifecyclePhase {
  CAMPAIGN("campaign"),
  INTEREST_CAPTURED("interest"),
  CUSTOMER_HANDOFF("customer"),
  SALES_ORDER("order"),
  INVOICE("invoice"),
  PAYMENT("payment"),
  FULFILLMENT("fulfillment"),
  SUPPORT_CASE("support"),
  RETURN_AUTHORIZATION("return"),
  REFUND("refund");

  private final String displayName;

  LifecyclePhase(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
