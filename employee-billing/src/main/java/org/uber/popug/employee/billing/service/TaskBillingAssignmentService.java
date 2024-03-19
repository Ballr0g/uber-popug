package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.domain.aggregates.TaskWithAssignee;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;

public interface TaskBillingAssignmentService {

    TaskWithAssignee assembleTaskWithAssignee(TaskReplicationInfo taskReplicationInfo);

}
