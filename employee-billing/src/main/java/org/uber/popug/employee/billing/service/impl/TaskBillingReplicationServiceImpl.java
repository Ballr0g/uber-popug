package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.task.replication.TaskReplicationInfo;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV2;
import org.uber.popug.employee.billing.mapping.TasksCUDKafkaEventMapper;
import org.uber.popug.employee.billing.mapping.TasksPersistenceMapper;
import org.uber.popug.employee.billing.repository.TaskRepository;
import org.uber.popug.employee.billing.service.TaskBillingAssignmentService;
import org.uber.popug.employee.billing.service.TaskBillingReplicationService;

@RequiredArgsConstructor
public class TaskBillingReplicationServiceImpl implements TaskBillingReplicationService {

    private final TasksCUDKafkaEventMapper tasksCUDKafkaEventMapper;
    private final TaskBillingAssignmentService taskBillingAssignmentService;
    private final TaskRepository taskRepository;
    private final TasksPersistenceMapper tasksPersistenceMapper;

    @Override
    public void replicateTaskToBilling(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1) {
        final var taskCreationInfo = tasksCUDKafkaEventMapper.toBusiness(taskCreatedReplicationEventV1);
        replicateTaskToBillingCommon(taskCreationInfo);
    }

    @Override
    public void replicateTaskToBilling(TaskCreatedReplicationEventV2 taskCreatedReplicationEventV2) {
        // Todo: validate lack of Jira ID in description
        final var taskCreationInfo = tasksCUDKafkaEventMapper.toBusiness(taskCreatedReplicationEventV2);
        replicateTaskToBillingCommon(taskCreationInfo);
    }

    private void replicateTaskToBillingCommon(TaskReplicationInfo taskReplicationInfo) {
        final var replicatedTaskWithAssignee = taskBillingAssignmentService.assembleTaskWithAssignee(taskReplicationInfo);
        final var replicatedTaskEntity = tasksPersistenceMapper.fromBusiness(replicatedTaskWithAssignee);

        taskRepository.saveReplicated(replicatedTaskEntity);
    }

}
