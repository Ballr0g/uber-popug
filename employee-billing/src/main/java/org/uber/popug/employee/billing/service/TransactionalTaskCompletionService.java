package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

public interface TransactionalTaskCompletionService {

    TaskWithAssignee performTransactionalCompletion(TaskWithAssignee taskForCompletion);

}
