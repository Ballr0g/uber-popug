package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.event.business.TaskCompletedEvent;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TaskCompletionService;
import org.uber.popug.employee.billing.service.TransactionalTaskCompletionService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

@RequiredArgsConstructor
public class TaskCompletionServiceImpl implements TaskCompletionService {

    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;
    private final UserAccountMembershipCheckingService userAccountMembershipCheckingService;
    private final TransactionalTaskCompletionService transactionalTaskCompletionService;

    @Override
    public void handleTaskCompletion(TaskCompletedEvent taskReassignedEvent) {
        final var taskForCompletionRequest = tasksBusinessKafkaEventMapper.toBusiness(taskReassignedEvent);
        final var taskForCompletion = userAccountMembershipCheckingService
                .retrieveTaskForCompletionIfRequestValid(taskForCompletionRequest);
        transactionalTaskCompletionService.performTransactionalCompletion(taskForCompletion);
    }
}
