package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.BillingCycleProvider;
import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.kafka.producer.BillingOperationsCUDEventProducer;
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
    private final BillingOperationsCUDEventProducer billingOperationsCUDEventProducer;

    @Override
    public BillingOperationFullData createNewTaskBillingOperationLogEntry(TaskWithAssignee taskWithAssignee) {
        final var billingOperationAggregate = retrieveNewTaskBillingOperationAggregate(taskWithAssignee);
        final var billingOperationEntity = billingOperationsPersistenceMapper.fromBusiness(billingOperationAggregate);
        billingOperationsRepository.appendBillingOperationEntry(billingOperationEntity);
        billingOperationsCUDEventProducer.sendTaskCreatedReplicationEvent(billingOperationAggregate);

        return billingOperationAggregate;
    }

    @Override
    public BillingOperationFullData createReassignedTaskBillingOperationLogEntry(TaskWithAssignee taskWithAssignee) {
        final var billingOperationAggregate = retrieveReassignedTaskBillingOperationAggregate(taskWithAssignee);
        final var billingOperationEntity = billingOperationsPersistenceMapper.fromBusiness(billingOperationAggregate);
        billingOperationsRepository.appendBillingOperationEntry(billingOperationEntity);
        billingOperationsCUDEventProducer.sendTaskCreatedReplicationEvent(billingOperationAggregate);

        return billingOperationAggregate;
    }

    @Override
    public BillingOperationFullData createCompletedTaskBillingOperationLogEntry(TaskWithAssignee taskWithAssignee) {
        final var billingOperationAggregate = retrieveCompletedTaskBillingOperationAggregate(taskWithAssignee);
        final var billingOperationEntity = billingOperationsPersistenceMapper.fromBusiness(billingOperationAggregate);
        billingOperationsRepository.appendBillingOperationEntry(billingOperationEntity);
        billingOperationsCUDEventProducer.sendTaskCreatedReplicationEvent(billingOperationAggregate);

        return billingOperationAggregate;
    }

    private BillingOperationFullData retrieveNewTaskBillingOperationAggregate(TaskWithAssignee taskWithAssignee) {
        final var billingOperation = billingOperationAssemblingService.assembleForNewlyAssignedTask(taskWithAssignee);
        return BillingOperationFullData.assembleBillingOperationLogEntry(
                billingOperation,
                taskWithAssignee.assignee(),
                billingCycleProvider
        );
    }

    private BillingOperationFullData retrieveReassignedTaskBillingOperationAggregate(TaskWithAssignee taskWithAssignee) {
        final var billingOperation = billingOperationAssemblingService.assembleForReassignedTask(taskWithAssignee);
        return BillingOperationFullData.assembleBillingOperationLogEntry(
                billingOperation,
                taskWithAssignee.assignee(),
                billingCycleProvider
        );
    }

    private BillingOperationFullData retrieveCompletedTaskBillingOperationAggregate(TaskWithAssignee taskWithAssignee) {
        final var billingOperation = billingOperationAssemblingService.assembleForCompletedTask(taskWithAssignee);
        return BillingOperationFullData.assembleBillingOperationLogEntry(
                billingOperation,
                taskWithAssignee.assignee(),
                billingCycleProvider
        );
    }

}
