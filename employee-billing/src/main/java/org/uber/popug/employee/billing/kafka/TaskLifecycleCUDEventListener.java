package org.uber.popug.employee.billing.kafka;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.event.cud.TaskCreatedReplicationEvent;

@Service
@KafkaListener(
        topics = "${kafka.listener.task-lifecycle-stream.topics}",
        groupId = "${kafka.listener.task-lifecycle-stream.group-id}",
        containerFactory = "tasksCUDListenerContainerFactory"
)
public class TaskLifecycleCUDEventListener {

    @KafkaHandler
    public void handleTaskCreatedCUDEvent(TaskCreatedReplicationEvent taskCreatedReplicationEvent) {
        // Todo: message handling logic here.
    }

}
