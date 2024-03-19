package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.BillingOperationFullData;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

public interface BillingOperationLogService {

    BillingOperationFullData createBillingOperationLogEntry(TaskWithAssignee taskWithAssignee);

}
