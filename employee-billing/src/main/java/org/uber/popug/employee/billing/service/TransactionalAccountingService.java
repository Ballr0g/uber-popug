package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

// Todo: decouple operations from Task concept (use concepts of recipient and payment)
public interface TransactionalAccountingService {

    void billForNewlyAssignedTask(TaskWithAssignee taskWithAssignee);

    void billForReassignedTask(TaskWithAssignee taskWithNewAssignee);

    void billForCompletedTask(TaskWithAssignee completedTaskWithAssignee);

}
