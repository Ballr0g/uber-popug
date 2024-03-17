package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.TaskInfo;

public interface TaskBillingAssignmentService {

    TaskWithAssignee assembleTaskWithAssignee(TaskInfo taskInfo);

}
