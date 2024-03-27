package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.domain.task.reassignment.TaskReassignmentInfo;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV1;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskReassignedEventV2;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TaskReassignmentService;
import org.uber.popug.employee.billing.service.TransactionalTaskReassignmentService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

@RequiredArgsConstructor
public class TaskReassignmentServiceImpl implements TaskReassignmentService {

    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;
    private final UserAccountMembershipCheckingService userAccountMembershipCheckingService;
    private final TransactionalTaskReassignmentService transactionalTaskReassignmentService;


    @Override
    public void handleTaskReassignment(TaskReassignedEventV1 taskReassignedEvent) {
        final var taskForReassignmentRequest = tasksBusinessKafkaEventMapper.toBusiness(taskReassignedEvent);
        handleTaskReassignmentCommon(taskForReassignmentRequest);
    }

    @Override
    public void handleTaskReassignment(TaskReassignedEventV2 taskReassignedEventV2) {
        final var taskForReassignmentRequest = tasksBusinessKafkaEventMapper.toBusiness(taskReassignedEventV2);
        handleTaskReassignmentCommon(taskForReassignmentRequest);
    }

    private void handleTaskReassignmentCommon(TaskReassignmentInfo taskReassignmentInfo) {
        final var taskForReassignment = userAccountMembershipCheckingService
                .retrieveTaskForReassignmentIfRequestValid(taskReassignmentInfo);
        transactionalTaskReassignmentService.performTransactionalReassignment(taskForReassignment);
    }

}
