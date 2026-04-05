package com.github.nambuntu.netsuite.ch04.model;

public enum BusinessMoment {
  SIGNED_UP_FOR_UPDATES("Theo signs up for Nova Fox updates"),
  OPENED_OPPORTUNITY("Theo asks about Nova Fox pre-order options"),
  BECAME_PAYING_CUSTOMER("Theo becomes a billable AI-Pets customer"),
  PLACED_ORDER("Theo places sales order so-theo-1001"),
  BILLED_ORDER("AI-Pets bills order so-theo-1001"),
  RECORDED_PAYMENT("AI-Pets records payment for so-theo-1001"),
  FULFILLED_ORDER("Jules fulfills order so-theo-1001"),
  OPENED_SUPPORT_CASE("Mira opens case case-theo-color-mismatch"),
  APPROVED_RETURN("AI-Pets approves return ra-theo-1001");

  private final String scenarioLabel;

  BusinessMoment(String scenarioLabel) {
    this.scenarioLabel = scenarioLabel;
  }

  public String scenarioLabel() {
    return scenarioLabel;
  }
}
