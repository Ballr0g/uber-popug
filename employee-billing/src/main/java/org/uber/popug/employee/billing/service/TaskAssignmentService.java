package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;

public interface TaskAssignmentService {

    void handleTaskAssignment(TaskCreatedEvent taskCreatedEvent);

}
