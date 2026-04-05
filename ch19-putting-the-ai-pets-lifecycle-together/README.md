# Chapter 19 - Putting the AI-Pets lifecycle together

This chapter project is a standalone Java example for one narrow lesson:

- chapter-scoped slices can still form one coherent commercial lifecycle
- identity continuity matters more than a giant shared framework
- recap orchestration should compose earlier ideas, not absorb them
- the final observable result should be a readable lifecycle timeline

The code is intentionally chapter-local. It does not depend on `sample` at runtime or wire earlier chapter projects together directly.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch19/model` for the recap scenario, lifecycle events, and final result
- `src/main/java/com/github/nambuntu/netsuite/ch19/facade` for the thin recap facades across CRM, orders, support, and returns
- `src/main/java/com/github/nambuntu/netsuite/ch19/view` for the lifecycle timeline renderer
- `src/main/java/com/github/nambuntu/netsuite/ch19` for the orchestrator and runnable demo

## Why this is safer

This chapter is a recap, not a new framework. It proves continuity by preserving the business identifiers that matter and passing them cleanly through thin lifecycle steps. That keeps the seams readable while still making the full story visible end to end.

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch19.AiPetsLifecycleDemo
```

## Expected output

```text
campaign-ai-pets-launch -> lead-theo-tran -> customer-theo-tran
customer-theo-tran -> so-theo-1001 -> inv-theo-1001 -> payment-theo-1001
so-theo-1001 -> fulfill-theo-1001
so-theo-1001 -> case-theo-color-mismatch -> ra-theo-1001 -> replacement-theo-1001
ra-theo-1001 -> refund-theo-1001 -> case-closed
```
