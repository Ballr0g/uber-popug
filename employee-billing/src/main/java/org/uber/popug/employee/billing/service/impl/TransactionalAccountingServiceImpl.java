package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.uber.popug.employee.billing.domain.aggregates.BillingAccountWithOwner;
import org.uber.popug.employee.billing.domain.aggregates.PaymentDataWithUser;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.PaymentData;
import org.uber.popug.employee.billing.domain.user.User;
import org.uber.popug.employee.billing.service.BillingAccountManagementService;
import org.uber.popug.employee.billing.service.BillingOperationLogService;
import org.uber.popug.employee.billing.service.TransactionalAccountingService;

@RequiredArgsConstructor
public class TransactionalAccountingServiceImpl implements TransactionalAccountingService {

    private final BillingOperationLogService billingOperationLogService;
    private final BillingAccountManagementService billingAccountManagementService;

    @Override
    @Transactional
    public void billForNewlyAssignedTask(TaskWithAssignee taskWithAssignee) {
        billingOperationLogService.createNewTaskBillingOperationLogEntry(taskWithAssignee);
        debitUser(
                taskWithAssignee.assignee(),
                PaymentData.newCreditData(taskWithAssignee.task().costs().assignmentCost())
        );
    }

    @Override
    public void billForReassignedTask(TaskWithAssignee taskWithNewAssignee) {
        billingOperationLogService.createReassignedTaskBillingOperationLogEntry(taskWithNewAssignee);
        debitUser(
                taskWithNewAssignee.assignee(),
                PaymentData.newCreditData(taskWithNewAssignee.task().costs().assignmentCost())
        );
    }

    private BillingAccountWithOwner debitUser(User user, PaymentData paymentData) {
        final var userToPayment = new PaymentDataWithUser(
                paymentData,
                user
        );
        return billingAccountManagementService.debitFromUser(userToPayment);
    }

}
