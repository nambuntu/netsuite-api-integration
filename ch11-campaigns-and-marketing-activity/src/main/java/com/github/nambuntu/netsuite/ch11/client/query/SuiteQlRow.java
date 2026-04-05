package com.github.nambuntu.netsuite.ch11.client.query;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class SuiteQlRow {

  private final Map<String, Object> values;

  public SuiteQlRow(Map<String, Object> values) {
    this.values = new LinkedHashMap<>();
    values.forEach((key, value) -> this.values.put(normalize(key), value));
  }

  public String string(String key) {
    Object value = values.get(normalize(key));
    return value == null ? null : value.toString();
  }

  public Instant instant(String key) {
    Object value = values.get(normalize(key));
    return value instanceof Instant instant ? instant : Instant.parse(value.toString());
  }

  private static String normalize(String key) {
    return key.toLowerCase(Locale.ROOT);
  }
}
