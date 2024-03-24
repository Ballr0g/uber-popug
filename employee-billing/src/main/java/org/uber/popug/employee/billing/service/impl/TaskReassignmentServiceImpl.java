package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.event.business.TaskReassignedEvent;
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
    public void handleTaskReassignment(TaskReassignedEvent taskReassignedEvent) {
        final var taskForReassignmentRequest = tasksBusinessKafkaEventMapper.toBusiness(taskReassignedEvent);
        final var taskForReassignment = userAccountMembershipCheckingService
                .retrieveTaskForReassignmentIfRequestValid(taskForReassignmentRequest);
        transactionalTaskReassignmentService.performTransactionalReassignment(taskForReassignment);
    }

}
