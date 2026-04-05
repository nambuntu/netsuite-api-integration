package com.github.nambuntu.netsuite.ch05;

public final class AiPetsCommercialLifecycleDemo {

  private AiPetsCommercialLifecycleDemo() {
  }

  public static void main(String[] args) {
    CommercialLifecycleTimelineBuilder builder = new CommercialLifecycleTimelineBuilder();
    LifecycleTimeline timeline = builder.buildTheoLifecycleTimeline();

    System.out.println("AI-Pets commercial lifecycle for Theo Tran");
    System.out.println(timeline.renderSummary());
  }
}
