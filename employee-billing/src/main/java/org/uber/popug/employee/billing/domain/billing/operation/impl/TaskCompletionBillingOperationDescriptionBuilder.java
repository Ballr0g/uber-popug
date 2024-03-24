package org.uber.popug.employee.billing.domain.billing.operation.impl;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationDescriptionBuilder;

public class TaskCompletionBillingOperationDescriptionBuilder
        implements BillingOperationDescriptionBuilder<TaskWithAssignee> {

    private static final String TASK_COMPLETED_LOG_OPERATION_DESCRIPTION_MESSAGE_TEMPLATE
            = "[Task completed] User with id %s, login %s completed a task with id = %s and description: %s, " +
              "earning %d$.";

    @Override
    public String buildBillingOperationDescription(TaskWithAssignee operationDescriptionSource) {
        return TASK_COMPLETED_LOG_OPERATION_DESCRIPTION_MESSAGE_TEMPLATE
                .formatted(
                        operationDescriptionSource.assignee().extPublicId(),
                        operationDescriptionSource.assignee().login(),
                        operationDescriptionSource.task().extPublicId(),
                        operationDescriptionSource.task().description(),
                        operationDescriptionSource.task().costs().completionCost()
                );
    }

}
