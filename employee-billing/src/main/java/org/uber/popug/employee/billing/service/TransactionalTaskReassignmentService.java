package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

public interface TransactionalTaskReassignmentService {

    TaskWithAssignee performTransactionalReassignment(TaskForReassignment taskForReassignment);

}
