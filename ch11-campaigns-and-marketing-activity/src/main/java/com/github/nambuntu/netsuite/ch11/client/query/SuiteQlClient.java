package com.github.nambuntu.netsuite.ch11.client.query;

import com.github.nambuntu.netsuite.ch11.query.SuiteQlQuery;
import java.util.List;
import java.util.Map;

public interface SuiteQlClient {

  <T> List<T> query(SuiteQlQuery<T> query, Map<String, Object> params);
}
