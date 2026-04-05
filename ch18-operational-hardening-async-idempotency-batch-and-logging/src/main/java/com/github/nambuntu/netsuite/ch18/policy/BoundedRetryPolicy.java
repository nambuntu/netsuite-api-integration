package com.github.nambuntu.netsuite.ch18.policy;

import java.util.Objects;

public final class BoundedRetryPolicy {

  private final int maxAttempts;

  public BoundedRetryPolicy(int maxAttempts) {
    if (maxAttempts < 1) {
      throw new IllegalArgumentException("maxAttempts must be at least 1");
    }
    this.maxAttempts = maxAttempts;
  }

  public int maxAttempts() {
    return maxAttempts;
  }

  public boolean shouldRetry(int attempt, RuntimeException failure) {
    Objects.requireNonNull(failure, "failure");
    return attempt < maxAttempts;
  }
}
