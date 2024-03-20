package org.uber.popug.employee.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.uber.popug.employee.billing.kafka.event.business.TaskCreatedEvent;
import org.uber.popug.employee.billing.mapping.TasksBusinessKafkaEventMapper;
import org.uber.popug.employee.billing.service.TaskAssignmentService;
import org.uber.popug.employee.billing.service.UserAccountBillingService;
import org.uber.popug.employee.billing.service.UserAccountMembershipCheckingService;

@RequiredArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TasksBusinessKafkaEventMapper tasksBusinessKafkaEventMapper;
    private final UserAccountMembershipCheckingService accountMembershipCheckingService;
    private final UserAccountBillingService userAccountBillingService;

    @Override
    public void handleTaskAssignment(TaskCreatedEvent taskCreatedEvent) {
        final var taskForBillingCandidate = tasksBusinessKafkaEventMapper.toBusiness(taskCreatedEvent);
        final var taskForBilling = accountMembershipCheckingService
                .retrieveTaskWithAssigneeIfRequestValid(taskForBillingCandidate);

        userAccountBillingService.billUserForTaskAssignment(taskForBilling);
    }

}
