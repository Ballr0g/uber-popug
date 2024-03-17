package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.aggregates.BillingCycleProvider;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.mapping.BillingOperationsPersistenceMapper;
import org.uber.popug.employee.billing.repository.ImmutableBillingOperationsRepository;
import org.uber.popug.employee.billing.service.TaskBillingOperationAssemblingService;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;

@RequiredArgsConstructor
public class TransactionalAccountingServiceImpl implements TransactionalAccountingService {

    private final TaskBillingOperationAssemblingService billingOperationAssemblingService;
    private final BillingCycleProvider billingCycleProvider;
    private final BillingOperationsPersistenceMapper billingOperationsPersistenceMapper;
    private final ImmutableBillingOperationsRepository billingOperationsRepository;

    @Override
    @Transactional
    public void billForNewlyAssignedTask(TaskWithAssignee taskWithAssignee) {
        final var billingOperationAggregate = retrieveBillingOperationAggregate(taskWithAssignee);
        final var billingOperationEntity = billingOperationsPersistenceMapper.fromBusiness(billingOperationAggregate);
        billingOperationsRepository.appendBillingOperationEntry(billingOperationEntity);
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
