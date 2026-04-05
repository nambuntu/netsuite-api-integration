# Chapter 7 - Sales orders: where intent becomes a business record

This chapter project is a standalone Java example for one narrow lesson:

- accept a business-shaped order command
- resolve a known customer and known items from chapter-local fixtures
- create one durable sales order with a visible starting status
- print a compact summary the reader can inspect in one glance

The code is intentionally chapter-local. It does not depend on earlier chapter projects or the older `sample` module.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch07` for the chapter models, fixtures, repository, service, formatter, and demo
- `src/test/java/com/github/nambuntu/netsuite/ch07` for the chapter tests

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch07.SalesOrderDemo
```

## Expected output

```text
Created sales order so-theo-1001
Customer: customer-theo-tran
Status: PENDING_FULFILLMENT
Lines:
- Nimbus Cat, midnight blue x1
- Personality Pack: Quiet Evenings x1
- Apartment Accessory Bundle x1
Total lines: 3
```
