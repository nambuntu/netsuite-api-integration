# Chapter 16 - Record transformations in real workflows

This chapter project is a standalone Java example for one narrow lesson:

- a downstream record often begins from an existing source record
- derive-then-patch keeps continuity more honestly than manual reassembly
- transforms preserve context, but account-specific choices still need explicit patching
- tests should prove continuity, not only the existence of a new record ID

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch16/model` for source, derived, and patch models
- `src/main/java/com/github/nambuntu/netsuite/ch16/service` for workflow, preview, and comparison services
- `src/main/java/com/github/nambuntu/netsuite/ch16/gateway` for the mock transform gateway
- `src/main/java/com/github/nambuntu/netsuite/ch16/store` for the in-memory chapter store
- `src/main/java/com/github/nambuntu/netsuite/ch16/fixture` for the inline Theo scenario builder
- `src/main/java/com/github/nambuntu/netsuite/ch16` for the runnable demo

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch16.TransformationWorkflowDemo
```

## Expected output

```text
source order: so-theo-1001
derived invoice: inv-theo-1001
same customer: customer-theo-tran
invoice source link: so-theo-1001
derived from source lines: yes
patched fields: memo, location, supportCaseLink
derived fulfillment: if-theo-1001
fulfillment source link: so-theo-1001
return from sales order: ra-theo-1001
return sales-order link: so-theo-1001
return from fulfillment: ra-theo-1001-from-fulfillment
return fulfillment link: if-theo-1001
manual assembly warning: source linkage must be re-copied explicitly
```
