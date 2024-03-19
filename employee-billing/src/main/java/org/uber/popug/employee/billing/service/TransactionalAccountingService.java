package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

public interface TransactionalAccountingService {

    void billForNewlyAssignedTask(TaskWithAssignee taskWithAssignee);

}
