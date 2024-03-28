package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV2;

public interface TaskReassignmentService {

    void handleTaskReassignment(TaskReassignedEventV1 taskReassignedEventV1);

    void handleTaskReassignment(TaskReassignedEventV2 taskReassignedEventV2);

}
