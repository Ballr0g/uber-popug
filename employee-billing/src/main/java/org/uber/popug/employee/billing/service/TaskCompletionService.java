package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;

public interface TaskCompletionService {

    void handleTaskCompletion(TaskCompletedEvent taskReassignedEvent);

}
