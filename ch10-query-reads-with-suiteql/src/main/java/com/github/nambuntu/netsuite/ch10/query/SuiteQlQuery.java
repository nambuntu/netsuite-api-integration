package com.github.nambuntu.netsuite.ch10.query;

import com.github.nambuntu.netsuite.ch10.client.query.SuiteQlRow;
import java.util.function.Function;

public record SuiteQlQuery<T>(String id, String sql, Function<SuiteQlRow, T> rowMapper) {
}
