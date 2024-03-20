package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;

public interface TaskReassignmentService {

    void handleTaskReassignment(TaskReassignedEvent taskReassignedEvent);

}
