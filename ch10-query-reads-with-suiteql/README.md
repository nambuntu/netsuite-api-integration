# Chapter 10 - Query reads with SuiteQL

This chapter project is a standalone Java example for one narrow lesson:

- keep query definitions in a small catalog
- return a compact reconciliation row instead of raw records
- separate read questions from write workflows
- make finance and fulfillment truth visible side by side

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch10/client/query` for the tiny SuiteQL client primitives
- `src/main/java/com/github/nambuntu/netsuite/ch10/query` for the query catalog
- `src/main/java/com/github/nambuntu/netsuite/ch10/model` for read models
- `src/main/java/com/github/nambuntu/netsuite/ch10/service` for the report service
- `src/main/java/com/github/nambuntu/netsuite/ch10/mock` for deterministic mock query data
- `src/main/java/com/github/nambuntu/netsuite/ch10` for the runnable demo

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch10.QueryReadsDemo
```

## Expected output

```text
Full reconciliation report
customer            order        invoice       invoiced  paid    due     payment  fulfillment  reconciliation
customer-theo-tran  so-theo-1001 inv-theo-1001 699.00    300.00  399.00  PARTIAL  SHIPPED      OPEN_BALANCE
customer-theo-tran  so-theo-1002 inv-theo-1002 49.00     49.00   0.00    PAID     PENDING      FULFILLMENT_LAG
customer-ava-cole   so-ava-1003  inv-ava-1003  149.00    149.00  0.00    PAID     SHIPPED      SETTLED

Attention report
customer            order        invoice       invoiced  paid    due     payment  fulfillment  reconciliation
customer-theo-tran  so-theo-1001 inv-theo-1001 699.00    300.00  399.00  PARTIAL  SHIPPED      OPEN_BALANCE
customer-theo-tran  so-theo-1002 inv-theo-1002 49.00     49.00   0.00    PAID     PENDING      FULFILLMENT_LAG
```
