package org.uber.popug.employee.billing.domain.billing.operation.impl;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationDescriptionBuilder;

public class TaskAssignmentBillingOperationDescriptionBuilder
        implements BillingOperationDescriptionBuilder<TaskWithAssignee> {

    private static final String TASK_ASSIGNED_LOG_OPERATION_DESCRIPTION_MESSAGE_TEMPLATE
            = "[Task assigned] User with id %s, login %s was assigned a task with id = %s and description: %s, " +
              "which costed %d$.";

    @Override
    public String buildBillingOperationDescription(TaskWithAssignee operationDescriptionSource) {
        return TASK_ASSIGNED_LOG_OPERATION_DESCRIPTION_MESSAGE_TEMPLATE
                .formatted(
                        operationDescriptionSource.assignee().extPublicId(),
                        operationDescriptionSource.assignee().login(),
                        operationDescriptionSource.task().extPublicId(),
                        operationDescriptionSource.task().description(),
                        operationDescriptionSource.task().costs().assignmentCost()
                );
    }

}
