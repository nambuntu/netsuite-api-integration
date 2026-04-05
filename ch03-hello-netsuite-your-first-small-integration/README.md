# Chapter 3 - Hello, NetSuite: your first small integration

This chapter project is a standalone Java example for one narrow workflow:

- accept Theo Tran's interest signup for `NOVA_FOX`
- derive or reuse a stable external ID
- upsert the contact interest through a write boundary
- read it back through a separate lookup boundary
- print a compact business-shaped result

The code is intentionally chapter-local. It does not depend on `sample` or any shared book framework.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch03/model` for chapter models
- `src/main/java/com/github/nambuntu/netsuite/ch03/service` for the workflow service
- `src/main/java/com/github/nambuntu/netsuite/ch03/client` for the write and read ports
- `src/main/java/com/github/nambuntu/netsuite/ch03/mock` for the in-memory mock runtime
- `src/test/java/com/github/nambuntu/netsuite/ch03/service` for the chapter tests

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch03.ContactInterestSyncRunner
```

## Expected output

```text
Received signup for Theo Tran and product NOVA_FOX
Syncing contact interest using external ID interest-theo-tran-nova-fox
Lookup confirmed synced record for Theo Tran <theo@aipets.example>
Campaign: campaign-ai-pets-launch
Status: synced
```
