package com.github.nambuntu.netsuite.ch07;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ItemFixtureRepository {

  private final Map<String, ItemDefinition> itemsByExternalId = new LinkedHashMap<>();

  public ItemFixtureRepository() {
    itemsByExternalId.put(
        "item-nimbus-cat-midnight-blue",
        new ItemDefinition("item-nimbus-cat-midnight-blue", "Nimbus Cat, midnight blue"));
    itemsByExternalId.put(
        "item-personality-pack-quiet-evenings",
        new ItemDefinition("item-personality-pack-quiet-evenings", "Personality Pack: Quiet Evenings"));
    itemsByExternalId.put(
        "item-accessory-bundle-apartment",
        new ItemDefinition("item-accessory-bundle-apartment", "Apartment Accessory Bundle"));
  }

  public List<SalesOrderLine> resolve(List<OrderLineCommand> commands) {
    List<SalesOrderLine> lines = new ArrayList<>();
    int lineNumber = 1;
    for (OrderLineCommand command : commands) {
      ItemDefinition item = itemsByExternalId.get(command.itemExternalId());
      if (item == null) {
        throw new IllegalArgumentException("unknown item external ID: " + command.itemExternalId());
      }
      lines.add(new SalesOrderLine(lineNumber, item.externalId(), item.displayName(), command.quantity()));
      lineNumber += 1;
    }
    return List.copyOf(lines);
  }
}
