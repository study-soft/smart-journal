package com.studysoft.smartjournal.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EntityNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException() {
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE, "Entity not found", Status.NOT_FOUND);
    }

    public EntityNotFoundException(String entityName) {
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE, entityName + " not found", Status.NOT_FOUND);
    }
}
