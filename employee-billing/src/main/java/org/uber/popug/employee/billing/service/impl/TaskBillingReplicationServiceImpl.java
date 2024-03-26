package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;
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
        final var replicatedTaskWithAssignee = taskBillingAssignmentService.assembleTaskWithAssignee(taskCreationInfo);
        final var replicatedTaskEntity = tasksPersistenceMapper.fromBusiness(replicatedTaskWithAssignee);

        taskRepository.saveReplicated(replicatedTaskEntity);
    }

}
