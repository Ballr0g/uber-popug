package org.uber.popug.employee.billing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;
import org.uber.popug.employee.billing.service.TaskAssignmentService;
import org.uber.popug.employee.billing.service.TaskCompletionService;
import org.uber.popug.employee.billing.service.TaskReassignmentService;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.task-workflow-actions.topics}",
        groupId = "${kafka.listener.task-workflow-actions.group-id}",
        containerFactory = "tasksBusinessWorkflowListenerContainerFactory",
        autoStartup = "false"
)
public class TaskWorkflowActionsBusinessEventListener {

    private final TaskAssignmentService taskAssignmentService;
    private final TaskReassignmentService taskReassignmentService;
    private final TaskCompletionService taskCompletionService;

    @KafkaHandler
    public void handleTaskCreatedBusinessEvent(TaskCreatedEvent taskCreatedEvent) {
        taskAssignmentService.handleTaskAssignment(taskCreatedEvent);
    }

    @KafkaHandler
    public void handleTaskReassignedBusinessEvent(TaskReassignedEvent taskReassignedEvent) {
        taskReassignmentService.handleTaskReassignment(taskReassignedEvent);
    }

    @KafkaHandler
    public void handleTaskCompletedBusinessEvent(TaskCompletedEvent taskCompletedEvent) {
        taskCompletionService.handleTaskCompletion(taskCompletedEvent);
    }

}
