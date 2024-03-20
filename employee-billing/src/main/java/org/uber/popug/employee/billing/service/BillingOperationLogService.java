package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

// Todo: leaked abstraction - decouple log handling from Task type.
public interface BillingOperationLogService {

    BillingOperationFullData createNewTaskBillingOperationLogEntry(TaskWithAssignee taskWithAssignee);

    BillingOperationFullData createReassignedTaskBillingOperationLogEntry(TaskWithAssignee taskWithAssignee);

}
