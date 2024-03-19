package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.BillingCycleProvider;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.mapping.BillingOperationsPersistenceMapper;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;
import org.uber.popug.employee.billing.service.BillingOperationLogService;
import org.uber.popug.employee.billing.service.TaskBillingOperationAssemblingService;

@RequiredArgsConstructor
public class BillingOperationLogServiceImpl implements BillingOperationLogService {

    private final TaskBillingOperationAssemblingService billingOperationAssemblingService;
    private final BillingCycleProvider billingCycleProvider;
    private final BillingOperationsPersistenceMapper billingOperationsPersistenceMapper;
    private final ImmutableBillingOperationsRepository billingOperationsRepository;

    @Override
    public BillingOperationFullData createBillingOperationLogEntry(TaskWithAssignee taskWithAssignee) {

        final var billingOperationAggregate = retrieveBillingOperationAggregate(taskWithAssignee);
        final var billingOperationEntity = billingOperationsPersistenceMapper.fromBusiness(billingOperationAggregate);
        billingOperationsRepository.appendBillingOperationEntry(billingOperationEntity);

        return billingOperationAggregate;
    }

    private BillingOperationFullData retrieveBillingOperationAggregate(TaskWithAssignee taskWithAssignee) {
        final var billingOperation = billingOperationAssemblingService.assembleForNewlyAssignedTask(taskWithAssignee);
        return BillingOperationFullData.assembleBillingOperationLogEntry(
                billingOperation,
                taskWithAssignee.assignee(),
                billingCycleProvider
        );
    }

}
