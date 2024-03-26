package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV1;

public interface TaskReassignmentService {

    void handleTaskReassignment(TaskReassignedEventV1 taskReassignedEventV1);

}
