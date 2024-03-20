package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperation;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationDescriptionBuilder;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperationIdProvider;
import org.uber.popug.employee.billing.service.TaskBillingOperationAssemblingService;

@RequiredArgsConstructor
public class TaskBillingOperationAssemblingServiceImpl implements TaskBillingOperationAssemblingService {

    private final BillingOperationIdProvider billingOperationIdProvider;
    private final BillingOperationDescriptionBuilder<TaskWithAssignee> newlyAssignedTaskDescriptionBuilder;
    private final BillingOperationDescriptionBuilder<TaskWithAssignee> reassignedTaskDescriptionBuilder;
    private final BillingOperationDescriptionBuilder<TaskWithAssignee> completedTaskDescriptionBuilder;

    @Override
    public BillingOperation assembleForNewlyAssignedTask(TaskWithAssignee newlyAssignedTask) {
        return BillingOperation.forAssignment(
                billingOperationIdProvider,
                newlyAssignedTaskDescriptionBuilder,
                newlyAssignedTask
        );
    }

    @Override
    public BillingOperation assembleForReassignedTask(TaskWithAssignee taskWithUpdatedAssignee) {
        return BillingOperation.forAssignment(
                billingOperationIdProvider,
                reassignedTaskDescriptionBuilder,
                taskWithUpdatedAssignee
        );
    }

    @Override
    public BillingOperation assembleForCompletedTask(TaskWithAssignee completedTaskWithAssignee) {
        return BillingOperation.forCompletion(
                billingOperationIdProvider,
                completedTaskDescriptionBuilder,
                completedTaskWithAssignee
        );
    }

}
