package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskForReassignment;
import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.creation.TaskAssignmentInfo;
import org.uber.popug.employee.billing.domain.task.completion.TaskCompletionInfo;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;

public interface UserAccountMembershipCheckingService {

    TaskWithAssignee retrieveTaskWithAssigneeIfRequestValid(TaskAssignmentInfo taskAssignmentInfo);

    TaskForReassignment retrieveTaskForReassignmentIfRequestValid(TaskReassignmentInfo taskReassignmentInfo);

    TaskWithAssignee retrieveTaskForCompletionIfRequestValid(TaskCompletionInfo taskCompletionInfo);

}
