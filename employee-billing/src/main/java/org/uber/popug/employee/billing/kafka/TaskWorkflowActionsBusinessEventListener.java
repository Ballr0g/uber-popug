package org.uber.popug.employee.billing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;
import org.uber.popug.employee.billing.service.UserAccountBillingService;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.task-workflow-actions.topics}",
        groupId = "${kafka.listener.task-workflow-actions.group-id}",
        containerFactory = "tasksBusinessWorkflowListenerContainerFactory"
)
public class TaskWorkflowActionsBusinessEventListener {

    private final UserAccountBillingService userAccountBillingService;

    @KafkaHandler
    public void handleTaskCreatedBusinessEvent(TaskCreatedEvent taskCreatedEvent) {
        userAccountBillingService.billUserForTaskAssignment(taskCreatedEvent);
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
