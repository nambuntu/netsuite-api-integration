package com.github.nambuntu.netsuite.ch11.query;

import com.github.nambuntu.netsuite.ch11.client.query.SuiteQlRow;
import java.util.Objects;
import java.util.function.Function;

public final class SuiteQlQuery<T> {

  private final String id;
  private final String sql;
  private final Function<SuiteQlRow, T> rowMapper;

  public SuiteQlQuery(String id, String sql, Function<SuiteQlRow, T> rowMapper) {
    this.id = Objects.requireNonNull(id, "id");
    this.sql = Objects.requireNonNull(sql, "sql");
    this.rowMapper = Objects.requireNonNull(rowMapper, "rowMapper");
  }

  public String id() {
    return id;
  }

  public String sql() {
    return sql;
  }

  public T mapRow(SuiteQlRow row) {
    return rowMapper.apply(row);
  }
}
