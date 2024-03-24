package org.uber.popug.employee.billing.exception.technical;

import lombok.Getter;
import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;

@Getter
public class TaskReassignmentFailedException extends RuntimeException {

    private static final String TASK_REASSIGNMENT_FAILED_MESSAGE_TEMPLATE
            = "Failed to reassign task with public id %s and assignee with public id %s to its new assignee: %s";

    private final TaskForReassignment failedTaskReassignment;

    public TaskReassignmentFailedException(TaskForReassignment failedTaskReassignment) {
        super(TASK_REASSIGNMENT_FAILED_MESSAGE_TEMPLATE.formatted(
                failedTaskReassignment.taskWithCurrentAssignee().task().extPublicId(),
                failedTaskReassignment.taskWithCurrentAssignee().assignee().extPublicId(),
                failedTaskReassignment.newAssignee().extPublicId()
        ));

        this.failedTaskReassignment = failedTaskReassignment;
    }

}
