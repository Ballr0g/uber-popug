package org.uber.popug.employee.billing.kafka.consumer.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCompletedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCreatedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV1;
import org.uber.popug.employee.billing.service.TaskAssignmentService;
import org.uber.popug.employee.billing.service.TaskCompletionService;
import org.uber.popug.employee.billing.service.TaskReassignmentService;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "${kafka.listener.task-workflow-actions.topics}",
        groupId = "${kafka.listener.task-workflow-actions.group-id}",
        containerFactory = "tasksBusinessWorkflowListenerContainerFactory"
)
public class TaskWorkflowActionsBusinessEventListener {

    private final TaskAssignmentService taskAssignmentService;
    private final TaskReassignmentService taskReassignmentService;
    private final TaskCompletionService taskCompletionService;

    @KafkaHandler
    public void handleTaskCreatedBusinessEvent(TaskCreatedEventV1 taskCreatedEventV1) {
        taskAssignmentService.handleTaskAssignment(taskCreatedEventV1);
    }

    @KafkaHandler
    public void handleTaskReassignedBusinessEvent(TaskReassignedEventV1 taskReassignedEventV1) {
        taskReassignmentService.handleTaskReassignment(taskReassignedEventV1);
    }

    @KafkaHandler
    public void handleTaskCompletedBusinessEvent(TaskCompletedEventV1 taskCompletedEventV1) {
        taskCompletionService.handleTaskCompletion(taskCompletedEventV1);
    }

}
