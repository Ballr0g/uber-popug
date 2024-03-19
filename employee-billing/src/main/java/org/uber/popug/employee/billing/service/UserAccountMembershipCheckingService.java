package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.creation.TaskForBillingAssignment;

public interface UserAccountMembershipCheckingService {

    TaskWithAssignee retrieveTaskWithAssigneeIfRequestValid(TaskForBillingAssignment task);

}
