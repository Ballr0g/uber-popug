package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskCompletedEventV1;

public interface TaskCompletionService {

    void handleTaskCompletion(TaskCompletedEventV1 taskCompletedEventV1);

}
