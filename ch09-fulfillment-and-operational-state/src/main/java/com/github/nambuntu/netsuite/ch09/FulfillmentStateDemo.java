package com.github.nambuntu.netsuite.ch09;

public final class FulfillmentStateDemo {

  private FulfillmentStateDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    OrderFulfillmentWorkflow.FulfillmentScenarioResult scenario = new OrderFulfillmentWorkflow().runTheoScenario();
    StringBuilder builder = new StringBuilder();
    appendSection(builder, null, scenario.initialState());
    appendSection(builder, "After packed event", scenario.afterPacked());
    appendSection(builder, "After first shipment", scenario.afterFirstShipment());
    appendSection(builder, "After second shipment", scenario.afterSecondShipment());
    builder.setLength(builder.length() - System.lineSeparator().length());
    return builder.toString();
  }

  private static void appendSection(
      StringBuilder builder,
      String heading,
      ProjectedOrderOperationalView view) {

    if (heading != null) {
      builder.append(heading).append(System.lineSeparator());
    } else {
      builder.append("Order: ").append(view.salesOrderExternalId()).append(System.lineSeparator());
      builder.append("Customer: ").append(view.customerExternalId()).append(System.lineSeparator());
    }
    builder.append("Financial state: ")
        .append(view.invoiceContext().financialState())
        .append(" (invoice ")
        .append(view.invoiceContext().invoiceExternalId())
        .append(")")
        .append(System.lineSeparator());
    builder.append("Operational state: ")
        .append(view.operationalState())
        .append(System.lineSeparator());
    builder.append("Lines:").append(System.lineSeparator());
    for (OrderOperationalLineView lineView : view.lineViews()) {
      builder.append("- ")
          .append(padDots(lineView.itemName(), 23))
          .append(" ordered ")
          .append(lineView.orderedQuantity())
          .append(", fulfilled ")
          .append(lineView.fulfilledQuantity())
          .append(System.lineSeparator());
    }
    builder.append("Fulfillment events:").append(System.lineSeparator());
    if (view.fulfillmentEvents().isEmpty()) {
      builder.append("- none");
    } else {
      for (ItemFulfillment fulfillment : view.fulfillmentEvents()) {
        builder.append("- ")
            .append(fulfillment.externalId())
            .append("  ")
            .append(String.format("%-7s", fulfillment.stage()))
            .append(" ")
            .append(fulfillment.shippingMethod())
            .append(System.lineSeparator());
      }
      builder.setLength(builder.length() - System.lineSeparator().length());
    }
    builder.append(System.lineSeparator()).append(System.lineSeparator());
  }

  private static String padDots(String value, int width) {
    if (value.length() >= width) {
      return value;
    }
    return value + " ".repeat(width - value.length()).replace(' ', '.');
  }
}
