package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.service.BillingOperationLogService;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;

@RequiredArgsConstructor
public class TransactionalAccountingServiceImpl implements TransactionalAccountingService {

    private final BillingOperationLogService billingOperationLogService;

    @Override
    @Transactional
    public void billForNewlyAssignedTask(TaskWithAssignee taskWithAssignee) {
        billingOperationLogService.createBillingOperationLogEntry(taskWithAssignee);
    }

}
