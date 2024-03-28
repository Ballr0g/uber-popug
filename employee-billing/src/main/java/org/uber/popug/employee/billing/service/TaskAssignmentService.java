package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV2;

public interface TaskAssignmentService {

    void handleTaskAssignment(TaskCreatedEventV1 taskCreatedEventV1);

    void handleTaskAssignment(TaskCreatedEventV2 taskCreatedEventV2);

}
