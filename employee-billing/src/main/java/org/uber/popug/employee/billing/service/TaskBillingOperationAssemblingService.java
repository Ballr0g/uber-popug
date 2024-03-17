package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.operation.BillingOperation;

public interface TaskBillingOperationAssemblingService {

    BillingOperation assembleForNewlyAssignedTask(TaskWithAssignee task);

}
