package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.billing.creation.TaskForBillingAssignment;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;

public interface UserAccountMembershipCheckingService {

    TaskWithAssignee retrieveTaskWithAssigneeIfRequestValid(TaskForBillingAssignment task);

    TaskForReassignment retrieveTaskForReassignmentIfRequestValid(TaskReassignmentInfo taskReassignmentInfo);

}
