package org.uber.popug.employee.billing.service;

import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

public interface TaskBillingReplicationService {

    void replicateTaskToBilling(TaskCreatedReplicationEvent taskCreatedReplicationEvent);

}
