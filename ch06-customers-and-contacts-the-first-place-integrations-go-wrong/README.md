# Chapter 6 - Customers and contacts: the first place integrations go wrong

This chapter project is a standalone Java example for one narrow lesson:

- resolve the customer first
- upsert the contact against that resolved customer
- update on repeated arrival instead of creating duplicates
- keep customer and contact external IDs distinct across the shared entity space

The code is intentionally chapter-local. It does not depend on earlier chapter projects or the older `sample` module.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch06/model` for chapter models
- `src/main/java/com/github/nambuntu/netsuite/ch06/store` for the in-memory entity store
- `src/main/java/com/github/nambuntu/netsuite/ch06/service` for lookup and upsert services
- `src/test/java/com/github/nambuntu/netsuite/ch06` for the chapter tests
- `src/test/resources` for the tiny Theo fixture file

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch06.CustomerContactDemo
```

## Expected output

```text
first sync  -> customer created  internalId=201  externalId=customer_theo_tran
first sync  -> contact created  internalId=305  externalId=contact_theo_tran_primary  company=201
second sync -> customer updated  internalId=201  externalId=customer_theo_tran
second sync -> contact updated  internalId=305  externalId=contact_theo_tran_primary  company=201
summary     -> customers=1 contacts=1 duplicateAvoidance=OK
```
