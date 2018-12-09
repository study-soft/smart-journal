package com.studysoft.smartjournal.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AccessDeniedException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AccessDeniedException() {
        super(ErrorConstants.FORBIDDEN_TYPE, "You have no permission to view it", Status.FORBIDDEN);
    }
}
