package org.uber.popug.employee.billing.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TaskAssignmentMismatchException extends RuntimeException {

    private static final String MISMATCHING_REQUEST_ASSIGNEE_MESSAGE_TEMPLATE
            = "Invalid assignment operation requested: Task with id %s is not assigned to user %s";

    private final UUID mismatchingTaskPublicId;
    private final UUID mismatchingAssigneePublicId;

    public TaskAssignmentMismatchException(UUID requestedTaskId, UUID requestedTaskAssigneeId) {
        super(MISMATCHING_REQUEST_ASSIGNEE_MESSAGE_TEMPLATE.formatted(requestedTaskId, requestedTaskAssigneeId));

        this.mismatchingTaskPublicId = requestedTaskId;
        this.mismatchingAssigneePublicId = requestedTaskAssigneeId;
    }

}
