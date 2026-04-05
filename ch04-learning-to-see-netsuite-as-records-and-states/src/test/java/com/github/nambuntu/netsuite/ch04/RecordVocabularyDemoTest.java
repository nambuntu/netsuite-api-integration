package com.github.nambuntu.netsuite.ch04;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RecordVocabularyDemoTest {

  @Test
  void demoOutputIncludesHeaderAndRepresentativeVocabularyLines() {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    try {
      System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
      RecordVocabularyDemo.main(new String[0]);
    } finally {
      System.setOut(originalOut);
    }

    String output = captured.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");

    assertThat(output).startsWith("AI-Pets record vocabulary\n");
    assertThat(output).contains("SIGNED_UP_FOR_UPDATES [Theo signs up for Nova Fox updates] -> contact -> relationship state -> write path");
    assertThat(output).contains("PLACED_ORDER [Theo places sales order so-theo-1001] -> salesOrder -> commitment state -> write path");
    assertThat(output).contains("OPENED_SUPPORT_CASE [Mira opens case case-theo-color-mismatch] -> supportCase -> post-sale support state -> write plus later read");
    assertThat(output).contains("APPROVED_RETURN [AI-Pets approves return ra-theo-1001] -> returnAuthorization -> controlled reverse-flow state -> write plus later read");
  }
}
