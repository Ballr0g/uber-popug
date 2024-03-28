package org.uber.popug.employee.billing.kafka.consumer.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedReplicationEventV2;
import org.uber.popug.employee.billing.service.TaskBillingReplicationService;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.task-lifecycle-stream.topics}",
        groupId = "${kafka.listener.task-lifecycle-stream.group-id}",
        containerFactory = "tasksCUDListenerContainerFactory"
)
public class TasksStreamCUDEventListener {

    private final TaskBillingReplicationService taskBillingReplicationService;

    @KafkaHandler
    public void handleTaskCreatedCUDEvent(TaskCreatedReplicationEventV1 taskCreatedReplicationEventV1) {
        // Todo: guarantee exactly-once semantics!
        taskBillingReplicationService.replicateTaskToBilling(taskCreatedReplicationEventV1);
    }

    @KafkaHandler
    public void handleTaskCreatedCUDEvent(TaskCreatedReplicationEventV2 taskCreatedReplicationEventV2) {
        taskBillingReplicationService.replicateTaskToBilling(taskCreatedReplicationEventV2);
    }

}
