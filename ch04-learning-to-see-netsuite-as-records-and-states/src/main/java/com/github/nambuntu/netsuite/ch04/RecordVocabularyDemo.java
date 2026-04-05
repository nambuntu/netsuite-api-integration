package com.github.nambuntu.netsuite.ch04;

import com.github.nambuntu.netsuite.ch04.model.RecordVocabularyEntry;
import com.github.nambuntu.netsuite.ch04.service.AiPetsRecordVocabularyService;

public final class RecordVocabularyDemo {

  private RecordVocabularyDemo() {
  }

  public static void main(String[] args) {
    AiPetsRecordVocabularyService service = new AiPetsRecordVocabularyService();

    System.out.println("AI-Pets record vocabulary");
    for (RecordVocabularyEntry entry : service.describeAll()) {
      System.out.println(entry.teachingLine());
    }
  }
}
