package com.github.nambuntu.netsuite.ch15;

import com.github.nambuntu.netsuite.ch15.fixture.ReturnFixtures;
import com.github.nambuntu.netsuite.ch15.fixture.ReturnsFixtureLoader;
import com.github.nambuntu.netsuite.ch15.model.ReturnProgressEvent;
import com.github.nambuntu.netsuite.ch15.service.ReturnAuthorizationService;
import com.github.nambuntu.netsuite.ch15.service.ReturnTimelineService;
import com.github.nambuntu.netsuite.ch15.service.ReturnWorkflowService;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import java.util.List;

public final class ReturnWorkflowDemo {

  private ReturnWorkflowDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    ReturnFixtures fixture = new ReturnsFixtureLoader().loadDefaultFixture();
    InMemoryReturnStore store = new InMemoryReturnStore();
    store.seedFixture(fixture);

    ReturnAuthorizationService returnAuthorizationService = new ReturnAuthorizationService(store);
    ReturnWorkflowService workflowService = new ReturnWorkflowService(returnAuthorizationService, store);
    ReturnTimelineService timelineService = new ReturnTimelineService(store);

    workflowService.authorizeExchange(fixture.exchangeAuthorizeCommand());
    workflowService.receiveReturn(fixture.exchangeReturnAuthorizationExternalId(), fixture.exchangeReceivedAt());
    workflowService.completeExchange(fixture.completeExchangeCommand());
    workflowService.authorizeRefund(fixture.refundAuthorizeCommand());
    workflowService.receiveReturn(fixture.finalReturnAuthorizationExternalId(), fixture.finalReceivedAt());
    workflowService.completeRefund(fixture.completeRefundCommand());

    return renderTimeline(fixture.supportCaseExternalId(), timelineService.findTimelineBySupportCase(fixture.supportCaseExternalId()));
  }

  private static String renderTimeline(String supportCaseExternalId, List<ReturnProgressEvent> timeline) {
    StringBuilder builder = new StringBuilder();
    builder.append(supportCaseExternalId).append(System.lineSeparator());
    for (ReturnProgressEvent event : timeline) {
      builder.append("  ")
          .append(String.format("%02d", event.sequenceNumber()))
          .append(" ")
          .append(String.format("%-17s", event.eventType()))
          .append(" ")
          .append(event.detail())
          .append(System.lineSeparator());
    }
    builder.setLength(builder.length() - System.lineSeparator().length());
    return builder.toString();
  }
}
