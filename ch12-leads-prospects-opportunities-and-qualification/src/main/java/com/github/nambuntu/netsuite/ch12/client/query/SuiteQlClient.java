package com.github.nambuntu.netsuite.ch12.client.query;

import com.github.nambuntu.netsuite.ch12.query.SuiteQlQuery;
import java.util.List;
import java.util.Map;

public interface SuiteQlClient {

  <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params);
}
