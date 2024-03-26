package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV1;

public interface TaskAssignmentService {

    void handleTaskAssignment(TaskCreatedEventV1 taskCreatedEventV1);

}
