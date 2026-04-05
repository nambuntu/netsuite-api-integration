package com.github.nambuntu.netsuite.ch10.client.query;

import java.util.List;

public record SuiteQlPage<T>(List<T> items, int offset, int limit, boolean hasMore, int totalResults) {
}
