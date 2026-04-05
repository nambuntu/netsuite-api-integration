package com.github.nambuntu.netsuite.ch10.client.query;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public record SuiteQlRow(Map<String, Object> values) {

  public SuiteQlRow {
    Map<String, Object> normalized = new LinkedHashMap<>();
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      normalized.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue());
    }
    values = Map.copyOf(normalized);
  }

  public String string(String field) {
    Object value = values.get(field.toLowerCase(Locale.ROOT));
    return value == null ? null : value.toString();
  }

  public BigDecimal decimal(String field) {
    Object value = values.get(field.toLowerCase(Locale.ROOT));
    if (value == null) {
      return null;
    }
    if (value instanceof BigDecimal decimal) {
      return decimal;
    }
    return new BigDecimal(value.toString());
  }
}
