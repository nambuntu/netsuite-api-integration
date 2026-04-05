package com.github.nambuntu.netsuite.ch15.command;

import com.github.nambuntu.netsuite.ch15.model.ReturnDisposition;
import com.github.nambuntu.netsuite.ch15.model.ReturnRequest;
import java.time.Instant;

public record AuthorizeReturnCommand(
    String returnAuthorizationExternalId,
    ReturnRequest request,
    ReturnDisposition disposition,
    String supportUpdateComment,
    Instant authorizedAt) {
}
