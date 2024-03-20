package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;

public interface UserAccountBillingService {

    void billUserForTaskAssignment(TaskWithAssignee taskForBilling);

    void billUserForTaskReassignment(TaskWithAssignee reassignedTaskForBilling);

}
