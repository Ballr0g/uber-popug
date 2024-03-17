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
    private final BillingOperationDescriptionBuilder<TaskWithAssignee> taskWithAssigneeDescriptionBuilder;

    @Override
    public BillingOperation assembleForNewlyAssignedTask(TaskWithAssignee task) {
        return BillingOperation.forTaskWithAssignee(
                billingOperationIdProvider,
                taskWithAssigneeDescriptionBuilder,
                task
        );
    }

}
