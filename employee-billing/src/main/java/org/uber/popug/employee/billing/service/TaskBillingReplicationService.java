package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV2;

public interface TaskBillingReplicationService {

    void replicateTaskToBilling(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1);

    void replicateTaskToBilling(TaskCreatedReplicationEventV2 taskCreatedReplicationEventV1);

}
