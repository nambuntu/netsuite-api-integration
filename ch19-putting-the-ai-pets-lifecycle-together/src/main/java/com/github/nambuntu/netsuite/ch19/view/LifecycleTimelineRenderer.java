package com.github.nambuntu.netsuite.ch19.view;

import com.github.nambuntu.netsuite.ch19.model.LifecycleResult;
import java.util.StringJoiner;

public final class LifecycleTimelineRenderer {

  public String render(LifecycleResult result) {
    StringJoiner joiner = new StringJoiner(System.lineSeparator());
    result.events().forEach(event -> joiner.add(event.summaryLine()));
    return joiner.toString();
  }
}
