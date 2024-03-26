package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;

public interface TaskBillingReplicationService {

    void replicateTaskToBilling(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1);

}
