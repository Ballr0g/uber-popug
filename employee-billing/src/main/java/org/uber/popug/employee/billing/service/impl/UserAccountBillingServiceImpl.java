package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;

@RequiredArgsConstructor
public class UserAccountBillingServiceImpl implements UserAccountBillingService {

    private final TransactionalAccountingService transactionalAccountingService;

    @Override
    public void billUserForTaskAssignment(TaskWithAssignee taskForBilling) {
        transactionalAccountingService.billForNewlyAssignedTask(taskForBilling);
    }

    @Override
    public void billUserForTaskReassignment(TaskWithAssignee reassignedTaskForBilling) {
        transactionalAccountingService.billForReassignedTask(reassignedTaskForBilling);
    }

    @Override
    public void payUserForTaskCompletion(TaskWithAssignee completedTaskForBilling) {
        transactionalAccountingService.billForCompletedTask(completedTaskForBilling);
    }

}
