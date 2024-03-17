package org.uber.popug.employee.billing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;
import org.uber.popug.employee.billing.service.TaskBillingReplicationService;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.task-lifecycle-stream.topics}",
        groupId = "${kafka.listener.task-lifecycle-stream.group-id}",
        containerFactory = "tasksCUDListenerContainerFactory"
)
public class TaskLifecycleCUDEventListener {

    private final TaskBillingReplicationService taskBillingReplicationService;

    @KafkaHandler
    public void handleTaskCreatedCUDEvent(TaskCreatedReplicationEvent taskCreatedReplicationEvent) {
        taskBillingReplicationService.replicateTaskToBilling(taskCreatedReplicationEvent);
    }

}
