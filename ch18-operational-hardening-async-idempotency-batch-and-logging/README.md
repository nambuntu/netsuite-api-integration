# Chapter 18 - Operational hardening: async, idempotency, batch, and logging

This chapter project is a standalone Java example for one narrow lesson:

- async handling is a workflow choice, not just a transport trick
- idempotency protects business state from duplicate execution
- retries should stay bounded and visible
- batch work should be chunked with per-item outcomes
- logs should carry correlation and business keys

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch18/model` for command, outcome, and journal-entry models
- `src/main/java/com/github/nambuntu/netsuite/ch18/store` for the idempotency store
- `src/main/java/com/github/nambuntu/netsuite/ch18/policy` for the bounded retry policy
- `src/main/java/com/github/nambuntu/netsuite/ch18/logging` for correlation-aware workflow context
- `src/main/java/com/github/nambuntu/netsuite/ch18/service` for the queue processor, journal, and batch planner
- `src/main/java/com/github/nambuntu/netsuite/ch18/mock` for the chapter-local fulfillment gateway
- `src/main/java/com/github/nambuntu/netsuite/ch18` for the runnable demo

## Why this is safer

This chapter gives idempotency a visible home. The same fulfillment command keeps one business command ID, one correlation ID, one bounded retry story, and one final outcome. That protects business state, not just transport neatness, because a replayed request does not create a second fulfillment.

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch18.OperationalHardeningDemo
```

## Expected output

```text
correlation=wf-2026-04-01-001
command=fulfill-order:so-theo-1001:shipment-1
attempts=2
deduplicated=yes
final-status=completed
replay-status=already-completed
batch-chunk=1/1
batch-item=fulfill-order:so-theo-1001:shipment-1 status=already-completed
batch-item=fulfill-order:so-theo-1002:shipment-1 status=completed
journal=submitted -> started#1 -> retrying#1 -> started#2 -> completed#2 -> duplicate-suppressed#2
```
