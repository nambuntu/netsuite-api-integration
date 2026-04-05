package com.github.nambuntu.netsuite.ch10.client.query;

import com.github.nambuntu.netsuite.ch10.query.SuiteQlQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SuiteQlClient {

  <T> SuiteQlPage<T> queryPage(SuiteQlQuery<T> query, Map<String, Object> params, int offset, int limit);

  default <T> List<T> queryAll(SuiteQlQuery<T> query, Map<String, Object> params) {
    List<T> items = new ArrayList<>();
    int offset = 0;
    int limit = 100;
    while (true) {
      SuiteQlPage<T> page = queryPage(query, params, offset, limit);
      items.addAll(page.items());
      if (!page.hasMore() || page.items().isEmpty()) {
        return List.copyOf(items);
      }
      offset += page.limit();
    }
  }

  default <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params) {
    return queryAll(query, params);
  }
}
