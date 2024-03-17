package org.uber.popug.employee.billing.kafka;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;

@Service
@KafkaListener(
        topics = "${kafka.listener.task-workflow-actions.topics}",
        groupId = "${kafka.listener.task-workflow-actions.group-id}",
        containerFactory = "smeApplicationKafkaListenerContainerFactory",
        autoStartup = "false"
)
public class TaskWorkflowActionsBusinessEventListener {

    @KafkaHandler
    public void handleTaskCreatedBusinessEvent(TaskCreatedEvent taskCreatedEvent) {
        throw new UnsupportedOperationException(
                "TaskWorkflowActionsBusinessEventListener.handleTaskCreatedBusinessEvent is not implemented."
        );
    }

    @KafkaHandler
    public void handleTaskReassignedBusinessEvent(TaskReassignedEvent taskReassignedEvent) {
        throw new UnsupportedOperationException(
                "TaskWorkflowActionsBusinessEventListener.handleTaskReassignedBusinessEvent is not implemented."
        );
    }

    @KafkaHandler
    public void handleTaskCompletedBusinessEvent(TaskCompletedEvent taskCompletedEvent) {
        throw new UnsupportedOperationException(
                "TaskWorkflowActionsBusinessEventListener.handleTaskCompletedBusinessEvent is not implemented."
        );
    }

}
