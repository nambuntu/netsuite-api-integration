package com.github.nambuntu.netsuite.ch15.fixture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nambuntu.netsuite.ch15.model.ReturnLine;
import com.github.nambuntu.netsuite.ch15.model.ReturnReason;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public final class ReturnsFixtureLoader {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public ReturnFixtures loadDefaultFixture() {
    try (InputStream inputStream = fixtureInputStream()) {
      if (inputStream == null) {
        throw new IllegalStateException("Missing fixture resource: /fixtures/ch15-returns-fixture.json");
      }
      JsonNode root = objectMapper.readTree(inputStream);
      return new ReturnFixtures(
          text(root, "supportCaseExternalId"),
          text(root, "customerExternalId"),
          text(root, "supportSubject"),
          text(root, "supportPriority"),
          text(root, "supportAssignee"),
          text(root, "originalSalesOrderExternalId"),
          text(root, "originalFulfillmentExternalId"),
          text(root, "originalOpenedComment"),
          instant(root, "caseOpenedAt"),
          text(root, "exchangeReturnAuthorizationExternalId"),
          instant(root, "exchangeAuthorizedAt"),
          instant(root, "exchangeReceivedAt"),
          instant(root, "exchangeCompletedAt"),
          text(root, "exchangeSupportComment"),
          line(root.get("exchangeReturnLine")),
          text(root, "replacementSalesOrderExternalId"),
          text(root, "replacementFulfillmentExternalId"),
          text(root, "replacementDescription"),
          text(root, "finalReturnAuthorizationExternalId"),
          instant(root, "finalAuthorizedAt"),
          instant(root, "finalReceivedAt"),
          instant(root, "finalCompletedAt"),
          text(root, "refundSupportComment"),
          line(root.get("refundReturnLine")),
          text(root, "creditMemoExternalId"),
          text(root, "customerRefundExternalId"),
          decimal(root, "refundAmount"),
          text(root, "refundResolutionComment"));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to load chapter 15 fixture", exception);
    }
  }

  private InputStream fixtureInputStream() throws IOException {
    InputStream classpathStream = ReturnsFixtureLoader.class.getResourceAsStream("/fixtures/ch15-returns-fixture.json");
    if (classpathStream != null) {
      return classpathStream;
    }
    Path filesystemPath = Path.of("src/test/resources/fixtures/ch15-returns-fixture.json");
    return Files.exists(filesystemPath) ? Files.newInputStream(filesystemPath) : null;
  }

  private static ReturnLine line(JsonNode node) {
    return new ReturnLine(
        text(node, "itemExternalId"),
        node.get("quantity").asInt(),
        text(node, "originalSalesOrderLineReference"),
        decimal(node, "amount"),
        ReturnReason.valueOf(text(node, "reason")));
  }

  private static String text(JsonNode node, String fieldName) {
    return node.get(fieldName).asText();
  }

  private static Instant instant(JsonNode node, String fieldName) {
    return Instant.parse(text(node, fieldName));
  }

  private static BigDecimal decimal(JsonNode node, String fieldName) {
    return new BigDecimal(text(node, fieldName));
  }
}
