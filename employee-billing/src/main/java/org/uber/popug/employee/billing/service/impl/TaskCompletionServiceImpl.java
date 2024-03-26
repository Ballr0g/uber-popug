package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.generated.dto.TaskCompletedEventV1;
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
    public void handleTaskCompletion(TaskCompletedEventV1 taskReassignedEventV1) {
        final var taskForCompletionRequest = tasksBusinessKafkaEventMapper.toBusiness(taskReassignedEventV1);
        final var taskForCompletion = userAccountMembershipCheckingService
                .retrieveTaskForCompletionIfRequestValid(taskForCompletionRequest);
        transactionalTaskCompletionService.performTransactionalCompletion(taskForCompletion);
    }
}
