package org.uber.popug.employee.billing.exception.technical;

import lombok.Getter;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

@Getter
public class TaskCompletionFailedException extends RuntimeException {

    private static final String TASK_REASSIGNMENT_FAILED_MESSAGE_TEMPLATE
            = "Failed to complete task with public id %s and assignee with public id %s";

    private final TaskWithAssignee failedTaskCompletion;

    public TaskCompletionFailedException(TaskWithAssignee failedTaskCompletion) {
        super(TASK_REASSIGNMENT_FAILED_MESSAGE_TEMPLATE.formatted(
                failedTaskCompletion.task().extPublicId(),
                failedTaskCompletion.assignee().extPublicId()
        ));

        this.failedTaskCompletion = failedTaskCompletion;
    }

}
