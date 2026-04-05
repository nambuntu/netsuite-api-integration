package com.github.nambuntu.netsuite.ch04.model;

public enum NetSuiteRecordTypeName {
  CONTACT("contact"),
  OPPORTUNITY("opportunity"),
  CUSTOMER("customer"),
  SALES_ORDER("salesOrder"),
  INVOICE("invoice"),
  CUSTOMER_PAYMENT("customerPayment"),
  ITEM_FULFILLMENT("itemFulfillment"),
  SUPPORT_CASE("supportCase"),
  RETURN_AUTHORIZATION("returnAuthorization");

  private final String apiName;

  NetSuiteRecordTypeName(String apiName) {
    this.apiName = apiName;
  }

  public String apiName() {
    return apiName;
  }
}
